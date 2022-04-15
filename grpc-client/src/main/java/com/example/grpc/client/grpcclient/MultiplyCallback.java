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
    private ManagedChannel channel;
    private MatrixServiceGrpc.MatrixServiceStub stub;
    GRPCClientService client;

    public MultiplyCallback(ArrayList<int[][]> currResult, ManagedChannel currChannel, GRPCClientService currObject){
        result=currResult;
        client=currObject;
        channel=currChannel;
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
        //let the client know the response has been handled
        client.status.total+=1;
        onCompleted();
          
        
      
    }
  
    @Override
    public void onError(Throwable cause) {
      
    }
  
    @Override
    public void onCompleted() {
      
    }

    public static Request getRequest(int[][] matrix1, int[][] matrix2){
		Builder request1= MatrixRequest.newBuilder();
		for(int i=0; i<matrix1.length;i++){
			
			Row.Builder rowBuilder = Row.newBuilder();
			for(int y=0; y<matrix1.length;y++){
				rowBuilder.addElements(matrix1[i][y]);
			}
			request1.addRows(rowBuilder.build());
		}

		Builder request2= MatrixRequest.newBuilder();
		for(int i=0; i<matrix2.length;i++){
			
			Row.Builder rowBuilder = Row.newBuilder();
			for(int y=0; y<matrix2.length;y++){
				rowBuilder.addElements(matrix2[i][y]);
			}
			request2.addRows(rowBuilder.build());
		}
		request1.setColCount(matrix1.length);
		request1.setRowCount(matrix1.length);
		request2.setColCount(matrix2.length);
		request2.setRowCount(matrix2.length);

		return Request.newBuilder()
		.setRequest1(request1.build())
		.setRequest2(request2.build())
		.build();

	}
  }
