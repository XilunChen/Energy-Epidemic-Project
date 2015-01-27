package opentsdbquery.system;

import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		
		OpenTSDBHttpConnection connection = new OpenTSDBHttpConnection("http://localhost:4242/");
		
		System.out.println("Enter aggregate function:");
		String aggregate = scanner.nextLine();
		
		System.out.println("Enter start:");
		String start = scanner.nextLine();
		
		System.out.println("Enter end:");
		String end = scanner.nextLine();
		
		System.out.println("Enter metric:");
		String metric = scanner.nextLine();
		
		System.out.println("Enter tag:");
		System.out.println("(Seperate <tag>=<value>,<tag>=<value>)");
		String tag = scanner.nextLine();
		scanner.close();
		
		try {
			switch(aggregate) {
				case "min":
					System.out.println(connection.getMin(start, end, metric, tag));
			
				case "sum":
					System.out.println(connection.getSum(start, end, metric, tag));
					
				case "max":
					System.out.println(connection.getMax(start, end, metric, tag));
			
				case "avg":
					System.out.println(connection.getAvg(start, end, metric, tag));
					
				case "dev":
					System.out.println(connection.getDev(start, end, metric, tag));
					
				default:
					System.out.println(connection.getAvg(start, end, metric, tag));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
