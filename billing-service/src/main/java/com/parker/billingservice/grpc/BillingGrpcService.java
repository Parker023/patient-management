package com.parker.billingservice.grpc;

import billing.BillingRequest;
import billing.BillingResponse;
import billing.BillingServiceGrpc.BillingServiceImplBase;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.logging.Logger;

@GrpcService
public class BillingGrpcService extends BillingServiceImplBase {
    @Override
    public void createBillingAccount(
            BillingRequest billingRequest,
            StreamObserver<BillingResponse> responseObserver) {
        Logger log = Logger.getLogger(BillingGrpcService.class.getName());
        BillingResponse billingResponse = BillingResponse.newBuilder()
                .setAccountId("12345")
                .setStatus("ACTIVE")
                .build();
        //uses to send response from grpc service to client(patient-service)
        responseObserver.onNext(billingResponse);
        //response is completed and we are ready for next cycle
        log.info("Create billing account successful with "+billingResponse.toString());
        responseObserver.onCompleted();
    }
}
