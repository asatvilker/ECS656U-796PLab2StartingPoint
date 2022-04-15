package com.example.grpc.client.grpcclient;

import java.util.ArrayList;
import java.util.List;

import com.example.grpc.server.grpcserver.MatrixReply;
import com.example.grpc.server.grpcserver.MatrixRequest;
import com.example.grpc.server.grpcserver.MatrixServiceGrpc;
import com.example.grpc.server.grpcserver.Request;
import com.example.grpc.server.grpcserver.Row;
import com.example.grpc.server.grpcserver.Element;
import com.example.grpc.server.grpcserver.MatrixRequest.Builder;

import io.grpc.ManagedChannel;
import io.grpc.stub.StreamObserver;

class MultiplyCallback implements StreamObserver<MatrixReply> {
    ArrayList<int[][]> result;

    public MultiplyCallback(ArrayList<int[][]> currResult){
        result=currResult;
       
    }

    @Override
    public void onNext(MatrixReply A) {
        int[][] tmp= {{0,0},{0,0}};

        List<Row> rowList = A.getRowsList();
        for(int z=0; z<rowList.size();z++){//convert result to native array type so it is in correct format
            for(int x=0; x<rowList.get(z).getElementsList().size();x++){
                tmp[z][x]=rowList.get(z).getElements(x);
               
            }
            
        } 
        
        result.add(tmp);
        //update array of results
    }
  
    @Override
    public void onError(Throwable cause) {
      
    }
  
    @Override
    public void onCompleted() {
      
    }

    
  }
