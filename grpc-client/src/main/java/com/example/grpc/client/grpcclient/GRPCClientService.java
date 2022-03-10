package com.example.grpc.client.grpcclient;

import com.example.grpc.server.grpcserver.PingRequest;
import com.example.grpc.server.grpcserver.PongResponse;
import com.example.grpc.server.grpcserver.Request;
import com.example.grpc.server.grpcserver.Row;
import com.example.grpc.server.grpcserver.MatrixRequest.Builder;
import com.example.grpc.server.grpcserver.PingPongServiceGrpc;
import com.example.grpc.server.grpcserver.MatrixRequest;

import java.util.List;

import com.example.grpc.client.grpcclient.PingPongEndpoint.Matrix;
import com.example.grpc.server.grpcserver.Element;
import com.example.grpc.server.grpcserver.MatrixReply;
import com.example.grpc.server.grpcserver.MatrixServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Service;
@Service
public class GRPCClientService {
    public String ping() {
        	ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9090)
                .usePlaintext()
                .build();        
		PingPongServiceGrpc.PingPongServiceBlockingStub stub
                = PingPongServiceGrpc.newBlockingStub(channel);        
		PongResponse helloResponse = stub.ping(PingRequest.newBuilder()
                .setPing("")
                .build());        
		channel.shutdown();        
		return helloResponse.getPong();
    }
    public String add(Matrix matrix1, Matrix matrix2){

		ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost",9090)
		.usePlaintext()
		.build();
		MatrixServiceGrpc.MatrixServiceBlockingStub stub = MatrixServiceGrpc.newBlockingStub(channel);
		Builder request1= MatrixRequest.newBuilder();
		for(int i=0; i<matrix1.rows;i++){
			String[] row = matrix1.matrix[i].split(",");
			Row.Builder rowBuilder = Row.newBuilder();
			for(int y=0; y<row.length;y++){
				rowBuilder.addElements(Integer.parseInt(row[y]));
			}
			request1.addRows(rowBuilder.build());
		}

		Builder request2= MatrixRequest.newBuilder();
		for(int i=0; i<matrix2.rows;i++){
			String[] row = matrix2.matrix[i].split(",");
			Row.Builder rowBuilder = Row.newBuilder();
			for(int y=0; y<row.length;y++){
				rowBuilder.addElements(Integer.parseInt(row[y]));
			}
			request2.addRows(rowBuilder.build());
		}
		request1.setColCount(matrix1.cols);
		request1.setRowCount(matrix1.rows);
		request2.setColCount(matrix2.cols);
		request2.setRowCount(matrix2.rows);

		Request combinedRequest = Request.newBuilder()
		.setRequest1(request1.build())
		.setRequest2(request2.build())
		.build();
		
		MatrixReply A=stub.addBlock(combinedRequest);

		String resp = "";

		List<Row> rowList = A.getRowsList();
		for(int i=0; i<rowList.size();i++){
			for(int x=0; x<rowList.get(i).getElementsList().size();x++){
				resp+=Integer.toString(rowList.get(i).getElements(x))+" ";
			}
			resp+="<br>";
		} 
		
		return resp;
    }

	public String multiply(Matrix matrix1, Matrix matrix2){

		ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost",9090)
		.usePlaintext()
		.build();
		MatrixServiceGrpc.MatrixServiceBlockingStub stub  = MatrixServiceGrpc.newBlockingStub(channel);
		
		
		Builder request1= MatrixRequest.newBuilder();
		for(int i=0; i<matrix1.rows;i++){
			String[] row = matrix1.matrix[i].split(",");
			Row.Builder rowBuilder = Row.newBuilder();
			for(int y=0; y<row.length;y++){
				rowBuilder.addElements(Integer.parseInt(row[y]));
			}
			request1.addRows(rowBuilder.build());
		}

		Builder request2= MatrixRequest.newBuilder();
		for(int i=0; i<matrix2.rows;i++){
			String[] row = matrix2.matrix[i].split(",");
			Row.Builder rowBuilder = Row.newBuilder();
			for(int y=0; y<row.length;y++){
				rowBuilder.addElements(Integer.parseInt(row[y]));
			}
			request2.addRows(rowBuilder.build());
		}
		request1.setColCount(matrix1.cols);
		request1.setRowCount(matrix1.rows);
		request2.setColCount(matrix2.cols);
		request2.setRowCount(matrix2.rows);

		Request combinedRequest = Request.newBuilder()
		.setRequest1(request1.build())
		.setRequest2(request2.build())
		.build();
		
		MatrixReply A=stub.multiplyBlock(combinedRequest);

		String resp = "";

		List<Row> rowList = A.getRowsList();
		for(int i=0; i<rowList.size();i++){
			for(int x=0; x<rowList.get(i).getElementsList().size();x++){
				resp+=Integer.toString(rowList.get(i).getElements(x))+" ";
			}
			resp+="<br>";
		} 
		
		return resp;
    }
}
