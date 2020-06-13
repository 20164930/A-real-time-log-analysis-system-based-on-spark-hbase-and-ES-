package sync;

import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequestBuilder;
import org.elasticsearch.action.update.UpdateRequestBuilder;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ESBulk {
    private static final int MAX_BULK_COUNT = 100;
    private static BulkRequestBuilder bulkRequestBuilder = null;

    private static final Lock commitLock = new ReentrantLock();

    private static ScheduledExecutorService scheduledExecutorService = null;

    static {
        // init es bulkRequestBuilder
        bulkRequestBuilder = ESTools.client.prepareBulk();

        // init thread pool and set size 1
        scheduledExecutorService = Executors.newScheduledThreadPool(1);

        // create beeper thread( it will be sync data to ES cluster)
        // use a commitLock to protected bulk es as thread-save
        final Runnable beeper = new Runnable() {
            public void run() {
                commitLock.lock();
                try {
                    bulkRequest(0);
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                } finally {
                    commitLock.unlock();
                }
            }
        };

        // set time bulk task
        // set beeper thread(10 second to delay first execution , 30 second period between successive executions)
        scheduledExecutorService.scheduleAtFixedRate(beeper, 10, 30, TimeUnit.SECONDS);

    }

    /**
     * shutdown time task immediately
     */
    public static void shutdownScheduEx() {
        if (null != scheduledExecutorService && !scheduledExecutorService.isShutdown()) {
            scheduledExecutorService.shutdown();
        }
    }

    /**
     * bulk request when number of builders is grate then threshold
     *
     * @param threshold
     */
    private static void bulkRequest(int threshold) {
        if (bulkRequestBuilder.numberOfActions() > threshold) {
            BulkResponse bulkItemResponse = bulkRequestBuilder.execute().actionGet();
            if (!bulkItemResponse.hasFailures()) {
                bulkRequestBuilder = ESTools.client.prepareBulk();
            }
        }
    }

    /**
     * add update builder to bulk
     * use commitLock to protected bulk as thread-save
     * @param builder
     */
    public static void addUpdateBuilderToBulk(UpdateRequestBuilder builder) {
        commitLock.lock();
        try {
            bulkRequestBuilder.add(builder);
            bulkRequest(MAX_BULK_COUNT);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            commitLock.unlock();
        }
    }

    /**
     * add delete builder to bulk
     * use commitLock to protected bulk as thread-save
     *
     * @param builder
     */
    public static void addDeleteBuilderToBulk(DeleteRequestBuilder builder) {
        commitLock.lock();
        try {
            bulkRequestBuilder.add(builder);
            bulkRequest(MAX_BULK_COUNT);
        } catch (Exception ex) {
            throw ex;
        } finally {
            commitLock.unlock();
        }
    }

}
