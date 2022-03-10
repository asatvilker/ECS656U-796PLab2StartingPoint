package com.example.grpc.server.grpcserver;


import com.example.grpc.server.grpcserver.MatrixReply.Builder;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
@GrpcService
public class MatrixServiceImpl extends MatrixServiceGrpc.MatrixServiceImplBase
{
	@Override
	public void addBlock(Request request, StreamObserver<MatrixReply> reply)
	{
		System.out.println("Request received from client:\n" + request);
		MatrixRequest request1= request.getRequest1();
		MatrixRequest request2= request.getRequest2();

		int[][] result = new int[request1.getRowCount()][request2.getColCount()];

		for(int i=0;i<request1.getRowCount();i++){    
			for(int j=0;j<request2.getColCount();j++){    
				result[i][j]=request1.getRows(i).getElements(j)+request2.getRows(i).getElements(j);   	
			}  
		}
		Builder response= MatrixReply.newBuilder();

		for(int i=0; i<result.length;i++){
			Row.Builder rowBuilder = Row.newBuilder();
			for(int y=0; y<result[i].length;y++){
				rowBuilder.addElements(result[i][y]);
			}
			response.addRows(rowBuilder.build());
		}
		
		reply.onNext(response.build());
		reply.onCompleted(); 
	}
	@Override
    	public void multiplyBlock(Request request, StreamObserver<MatrixReply> reply)
    	{
        	System.out.println("Request received from client:\n");
			

			MatrixRequest request1= request.getRequest1();
			MatrixRequest request2= request.getRequest2();
			int[][] result = new int[request1.getRowCount()][request2.getColCount()];

			for(int i=0;i<request1.getRowCount();i++){    
				for(int j=0;j<request2.getColCount();j++){    
					result[i][j]=0;   
					   
					for(int k=0;k<request1.getColCount();k++){      
						result[i][j]+=request1.getRows(i).getElements(k)*request2.getRows(k).getElements(j);      
					}
					
				}  
				
			}

			Builder response= MatrixReply.newBuilder();

			for(int i=0; i<result.length;i++){
				Row.Builder rowBuilder = Row.newBuilder();
				for(int y=0; y<result[i].length;y++){
					rowBuilder.addElements(result[i][y]);
				}
				response.addRows(rowBuilder.build());
			}
			
        	reply.onNext(response.build());
        	reply.onCompleted(); 
    }
}
