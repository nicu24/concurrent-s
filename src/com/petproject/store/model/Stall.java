package com.petproject.store.model;

import com.petproject.store.services.StorePerformanceService;
import org.w3c.dom.ls.LSOutput;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public class Stall {

    Logger log;
    List<Seller> sellers;
    AtomicInteger servedBuyers = new AtomicInteger(0);

    StorePerformanceService performanceService = new StorePerformanceService();



    public Stall(Logger log, List<Seller> sellers) {
        this.log = log;
        this.sellers = sellers;
    }


    Thread t2 = new Thread(new Runnable() {

        Queue<Buyer> buyers;
        @Override
        public void run() {
            sellers.get(0).serveTheBuyer(buyers.poll());
            servedBuyers.incrementAndGet();
        }
    });


    public void trade(Queue<Buyer> buyers, int n) {
        servedBuyers.set(0);
        performanceService.startServeBuyers();



//       sellers.stream().forEach(seller -> {
//           seller.serveTheBuyer(buyers.poll());
//          servedBuyers.incrementAndGet();
//       });

        //  First Seller

    sellers.get(0).serveTheBuyer(buyers.poll());
    servedBuyers.incrementAndGet();


//Other Seller
    for (int i = 1; i < n; i++) {
        Seller seller = sellers.get(i);
        new Thread(() -> {
            try {
                seller.serveTheBuyer(buyers.poll());
            }catch (java.lang.NullPointerException e){ log.info("not buyers");}
                servedBuyers.incrementAndGet();
        }).start();

    }



        log.info(performanceService.checkPerformance(servedBuyers.get()));
    }




}

