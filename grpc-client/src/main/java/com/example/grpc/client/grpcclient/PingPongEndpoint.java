package com.example.grpc.client.grpcclient;

import org.springframework.stereotype.Controller;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
public class PingPongEndpoint {    

	GRPCClientService grpcClientService;    
	@Autowired
    	public PingPongEndpoint(GRPCClientService grpcClientService) {
        	this.grpcClientService = grpcClientService;
    	}    
	
       
	@GetMapping("/")
	public String home () {
		return "uploadForm";
	}
	public class Matrix{
		int rows;
		int cols;
		String[] matrix;
		public Matrix(String text){
			matrix=text.split("\n");
			rows=matrix.length;
			checkMatrix();
			cols=matrix[0].split(",").length;

		}
		public void checkMatrix(){
			int[] cols= new int[rows];
			
			for(int i=0; i<rows;i++){
				String[] elements=matrix[i].split(",");
				cols[i]=elements.length;
			}
	
			int currentCol = cols[0];
			for(int i=1; i<cols.length;i++){
				if(cols[i] != currentCol){
					throw new SquareException("Columns must all have the same length");
				}
				currentCol=cols[i];
			}	
	
			if(currentCol == rows){
				return;
			}
			else{
				throw new SquareException("Please ensure the matrix is square");
			}
			
		}
	
		
	}

	
	
	public void checkEqual(Matrix matrix1, Matrix matrix2){
		
		if (matrix1.rows != matrix2.rows){
			throw new SquareException("Please ensure both matrices have the same number of rows");
		}
		else if (matrix1.cols != matrix2.cols){
			throw new SquareException("Please ensure both matrices have the same number of columns");
		}
		else{
			return;
		}
	}

	@PostMapping("/")
	public String handleFileUpload(@RequestParam("file") MultipartFile file,@RequestParam("file2") MultipartFile file2,@RequestParam("operation") String operation,@RequestParam("deadline") String deadline, 
			RedirectAttributes redirectAttributes) throws IOException{

			
		System.out.println(operation);
		String data1 = new String(file.getBytes());
		Matrix matrix1=new Matrix(data1);

		String data2 = new String(file2.getBytes());
		Matrix matrix2=new Matrix(data2);

		checkEqual(matrix1, matrix2);
		if(operation.equals("Addition")){
			redirectAttributes.addFlashAttribute("result", grpcClientService.add(matrix1,matrix2));
		}
		else{
			redirectAttributes.addFlashAttribute("result", grpcClientService.multiply(matrix1,matrix2,deadline));
		}
		
		redirectAttributes.addFlashAttribute("error","");
		

		return "redirect:/";
	}


	
 	public class SquareException extends RuntimeException {
		 String message;
		public SquareException(String msg) {
			message=msg;
        	//a custom exception we throw when the input is not of correct dimensions
    	}
 	}

	
	@ExceptionHandler(SquareException.class)//listens for exception above and returns error message
	public String handleError(RedirectAttributes redirectAttributes,SquareException ex) {
	
		redirectAttributes.addFlashAttribute("error",ex.message);
		return "redirect:/";
  }
}
