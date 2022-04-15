package com.example.grpc.client.grpcclient;

import com.example.grpc.server.grpcserver.PingRequest;
import com.example.grpc.server.grpcserver.PongResponse;
import com.example.grpc.server.grpcserver.Request;
import com.example.grpc.server.grpcserver.Row;
import com.example.grpc.server.grpcserver.MatrixRequest.Builder;
import com.example.grpc.server.grpcserver.PingPongServiceGrpc;
import com.example.grpc.server.grpcserver.MatrixRequest;

import java.util.ArrayList;
import java.util.Arrays;
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
	ArrayList<int[][]> Rij;

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

	public String multiply(Matrix matrix1, Matrix matrix2, String deadline)throws InterruptedException{

		int dim = matrix1.rows;
		int[][] result= new int[dim][dim];
		String resp = "";

		//create servers
		ManagedChannel channel1 = ManagedChannelBuilder.forAddress("localhost",9090).usePlaintext().build();
		ManagedChannel channel2 = ManagedChannelBuilder.forAddress("34.105.204.118",9090).usePlaintext().build();
		ManagedChannel channel3 = ManagedChannelBuilder.forAddress("34.70.212.23",9090).usePlaintext().build();
		ManagedChannel channel4 = ManagedChannelBuilder.forAddress("34.89.125.113",9090).usePlaintext().build();
		ManagedChannel channel5 = ManagedChannelBuilder.forAddress("35.189.90.120",9090).usePlaintext().build();
		ManagedChannel channel6 = ManagedChannelBuilder.forAddress("35.242.139.46",9090).usePlaintext().build();
		ManagedChannel channel7 = ManagedChannelBuilder.forAddress("35.246.61.223",9090).usePlaintext().build();
		ManagedChannel channel8 = ManagedChannelBuilder.forAddress("34.105.140.35",9090).usePlaintext().build();
		ManagedChannel[] channels= {channel1,channel2,channel3,channel4,channel5,channel6,channel7,channel8};
		
		//find number of servers needed
		int numServers = getServers(dim,matrix1, matrix2, channel1,deadline);
		System.out.println("numServers: "+Integer.toString(numServers));

		//find number of stubs per server
		int totalStubs=getTotalStubs(dim); //the number of stubs equal to number multiplications (regular multiplication but each element is a 2*2 matrix)
		int channelLength= (int)(totalStubs/numServers)+1; //the number of stubs per channel

		//create stubs
		ArrayList<ArrayList<MatrixServiceGrpc.MatrixServiceStub>> stubs = new ArrayList<>();//create array where each element is an array of stubs for that channel
		for(int i=0; i < numServers; i++) {
			stubs.add(new ArrayList<>());
			for(int x=0;x<channelLength && totalStubs>0;x++){
				stubs.get(i).add(MatrixServiceGrpc.newStub(channels[i]));
				totalStubs-=1;
			}
		}
		
		
		//calculate answer
		long startTime = System.nanoTime();
		for(int i=0;i<dim/2;i++){//iterate over rows
            for(int j=0;j<dim/2;j++){//iterate over elements in row
				Rij= new ArrayList<int[][]>();//a temporary result for each element (2*2 matrix) 
				for(int k=0;k<dim/2;k++){//multiply rows by columns      
                        int[][] Aik=getSubset(matrix1,i,k); //rather than multiplying elements, we multiply 2x2 matrices
                        int[][] Bkj=getSubset(matrix2,k,j);
						
						Request combinedRequest = getRequest(Aik, Bkj);//creates request in format to send to server
						MatrixServiceGrpc.MatrixServiceStub stub = stubs.get(0).get(0); //get stub at front of queue
						MultiplyCallback callback = new MultiplyCallback(Rij);//used to take the result and add it to the matrix for this element
						stub.multiplyBlock(combinedRequest, callback);
						  
						//remove stub from queue once used
						stubs.get(0).remove(0);
						if(stubs.get(0).isEmpty()){
							stubs.remove(0);
						}
						      
					}
			
				while(Rij.size()<dim/2){//wait for all multiplactions to finish
					Thread.sleep(1);
						
				}

				int[][] temp = {{0,0},{0,0}};
				MatrixServiceGrpc.MatrixServiceBlockingStub addStub = MatrixServiceGrpc.newBlockingStub(channel8);
				for(int s = 0; s<Rij.size();s++){//add up results to find total 2*2 matrix
					Request combinedAddition = getRequest(Rij.get(s), temp);
					MatrixReply B=addStub.addBlock(combinedAddition);
					List<Row> rowListB = B.getRowsList();
					for(int z=0; z<rowListB.size();z++){
						for(int x=0; x<rowListB.get(z).getElementsList().size();x++){
							temp[z][x]=rowListB.get(z).getElements(x);
						}			
					}
				}			
    
				for(int z=0; z<2;z++){//add the elements of our temporary result to our main result
					for(int x=0; x<2;x++){
						
						result[(i*2)+z][(j*2)+x]=temp[z][x];
					}
					
				} 
				
            }
        }
		long endTime = System.nanoTime();
		double timeTaken=(double)(endTime-startTime)/1_000_000_000;
		
		//build response from result
		for(int z=0; z<result.length;z++){
			for(int x=0; x<result.length;x++){
				resp+=Integer.toString(result[z][x])+" ";
			}
			resp+="<br>";
			
		} 
		
		resp+="<p>"+Double.toString(timeTaken)+"</p>";
		
		return resp;
	
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

	public static int[][] getSubset(Matrix matrix,int i, int k){
		int[][] subset= new int[2][2];

		for(int x=0;x<2;x++){
			for(int z=0;z<2;z++){
				subset[x][z]=Integer.parseInt(matrix.matrix[(i*2)+x].split(",")[(k*2)+z]);
			}
		}
		
        return subset;
    }

	public static double getTime(Matrix matrix1, Matrix matrix2 ,ManagedChannel channel){
		
		int[][] Aik=getSubset(matrix1,0,0); //rather than multiplying elements, we multiply 2x2 matrices
        int[][] Bkj=getSubset(matrix2,0,0);
						
		Request combinedRequest = getRequest(Aik, Bkj);

		MatrixServiceGrpc.MatrixServiceBlockingStub testStub =MatrixServiceGrpc.newBlockingStub(channel);
		long startTime = System.nanoTime();
		MatrixReply A=testStub.multiplyBlock(combinedRequest);
		long endTime = System.nanoTime();
		
		return (double)(endTime-startTime)/1_000_000_000;
	}

	public static int getServers(int dim,Matrix matrix1, Matrix matrix2 ,ManagedChannel channel, String deadline){
		
		int totalStubs=getTotalStubs(dim); //the number os stubs equal to number multiplications (regular multiplication but each element is a 2*2 matrix)

		double footprint = getTime(matrix1, matrix2, channel);
		System.out.println("Time taken for 1 call: "+Double.toString(footprint));
		System.out.println("Total stubs (no.of multiplication operations): "+Double.toString(totalStubs));
		int numServers = (int)(((footprint*totalStubs)/Double.parseDouble(deadline))+1);

		if(numServers>8){//ensuring we dont exceed max number of servers
			numServers=8;
		}
		return numServers;
	}

	public static int getTotalStubs(int dim){
		int totalSubsets = (dim*dim)/4;//we will be treating these subsets as our elements - each is 2*2 so total of 4 elements make up 1 element in new matrix
		int newDims=dim/2;//each element is a 2*2 now the new dimensions will be half
		int totalStubs=totalSubsets*newDims; //the number os stubs equal to number multiplications (regular multiplication but each element is a 2*2 matrix)
		return totalStubs;
	}

	
}

