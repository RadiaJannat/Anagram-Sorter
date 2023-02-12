/**
 * @author Radia Jannat
 * @version 2.6
 * UCID: 30118181
 * Email: radia.jannat@ucalgary.ca
 * Tutorial number: T06
 * TA's name: Zaman Wahid
 *
 */

import java.io.*;
import java.util.Scanner;

public class Assign2 {

    /**
     * 
     * @param args main receives two user input arguments that are to be used as input and 
     * output file names
     * @return nothing
     * @throws IOException
     */
    public static void main(String[] args) throws IOException { 

        //Error checking of arguments
        for(int r = 0; r < 2; r+=1){

            if(args[r].contains(".txt")){
                
                System.out.println("Do not add '.txt' to the input or output file name.");
                System.exit(1);
            }
        }

        String inputFileName = args[0] + ".txt"; 
        String outputFileName = args[1] + ".txt" ; 
        long timeTaken;

        long timer_start = System.nanoTime(); //Start time for processing an input file

        LinkedLists[] data = makeArray(inputFileName);
        data = removeExtra(data); 

        //Insertion Sort on each linked list
        for(int p=0; p < data.length; p+=1){ 

            if(data[p] != null){

                data[p].setHead(insertionSort(data[p].getHead()));
            }
        }

        int column = 0;
        int q = data.length - 1;

        //Quick Sort to on linked list array
        qSort(data, 0, q - 1, column);

        long timer_stop = System.nanoTime(); //End time for processing an input file
        timeTaken = timer_stop - timer_start; //Time taken to process an input file

        System.out.println("\nTime taken to process input file '" + inputFileName 
        + "' : " + timeTaken + " E-9 seconds.\n");

        PrintWriter pw = new PrintWriter(outputFileName);    
    
        for(int r = 0; r < data.length; r+=1){

            pw.println(data[r].toPrint()); 
        }
        pw.close();
        
    }

    
    /**
     * 
     * @param name This is a string from main which is the name of the input file 
     * to be used to make linked lists here
     * @return makeArray returns an array of linked lists (which could contain nulls)
     * @throws FileNotFoundException
     */
    public static LinkedLists[] makeArray(String name) throws FileNotFoundException{

        LinkedLists[] arrayLL = new LinkedLists [158000];

        /**
         * reference to input files:
         * https://www.geeksforgeeks.org/different-ways-reading-text-file-java/
         */
        File inputFile = new File(name);
        Scanner inFile = new Scanner(inputFile);

        int p = 0;
        while(inFile.hasNextLine()){

            String current = inFile.nextLine();
            String curr = current.replaceAll("[^A-Za-z]+", ""); 
            /**
             * reference to remove spaces and punctuations:
             * https://stackoverflow.com/questions/21946042/remove-all-spaces-and-punctuation-anything-not-a-letter-from-a-string
             */

            int Index = anagramOrNot(arrayLL, curr);
            if(Index == -1){

                arrayLL[p] = new LinkedLists(current);
                p+=1;
            }
            else{

                arrayLL[Index].addInEnd(current);
            }
        }
        inFile.close();

        return arrayLL;
    }


    /**
     * 
     * @param arrayLL This is an array of linked lists (that could contain nulls) 
     * @param curElement This is an element to check against arrayLL and see if curElement is 
     * an anagram of any element in arrayLL
     * @return returns and integer
     */
    public static int anagramOrNot(LinkedLists[] arrayLL, String curElement){

        /**
         * reference for this section:
         * https://www.baeldung.com/java-strings-anagrams
         */

        int p = 0;

        while(arrayLL[p] != null){ 

            String comparElement = arrayLL[p].getHead().getData(); 
            if(curElement.length() == comparElement.length()){ 

                comparElement = comparElement.trim();

                char[] sortedWord1 = result(comparElement);
                char[] sortedWord2 = result(curElement);

                int count = 0;
		        int size = comparElement.length();
		        for(int q=0; q<size;){

                    if (sortedWord1[q] != sortedWord2[q]){

				        q+=1;
			        }
			        else{

                        count+=1;
				        q+=1;
			        }
		        }

                if(count == size){

                    return p;
                }
                else{

                    p+=1;
                }

            }
            else{

                p+=1;
            }
        }
        return (-1);
    }


    /**
     * 
     * @param str1 This is a string word from method 'anagramOrNot' 
     * @return a char array of a letters of a word sortedin ascending order is returned
     */
    public static char[] result(String str1){

        char arr1[] = str1.toCharArray();

        /**
         * For method 'result' selection sort code was borrowed from notes in:
         * https://d2l.ucalgary.ca/d2l/le/content/426519/viewContent/5151501/View
         * adapted as required for sorting letters of a word in ascending order
         */
           
	        for(int i = 0; i < arr1.length - 1; i++){

		        int min_index = i;
		        char minStr = arr1[i];

		        for(int j = i + 1; j < arr1.length; j++){

			        if(arr1[j] < minStr){

				        minStr = arr1[j];
				        min_index = j;
                    }
                }
          
	        if(min_index != i){

		        char temp = arr1[min_index];
		        arr1[min_index] = arr1[i];
		        arr1[i] = temp;
            }
        }
        return arr1;
    }


    /**
     * 
     * @param arrayLL This is a linked list array (doesn't contain nulls)
     * @param low This is an integer that was 0 at main
     * @param high This is an integer value which was the linked list array length -1 in main
     * @param column This is an integer of the column of the linked list array we want to 
     * sort in alphabetic order
     * @return returns an integer value
     */
    static int partition(LinkedLists[] arrayLL, int low, int high, int column){

        /**
         * reference for quicksort for methods 'partition' and 'qSort':
         * https://stackoverflow.com/questions/35127804/sort-a-two-dimensional-array-by-column-using-quick-sort
         * code was borrowed and adapted as required for this assignment
         */
		String pivot = arrayLL[high].getHead().getData();
		int i = low - 1; 

		for (int j = low; j <= high - 1; j++){
			
			if (arrayLL[j].getHead().getData().charAt(0) <= pivot.charAt(0)) {
				i++;

				LinkedLists temp = arrayLL[i];
				arrayLL[i] = arrayLL[j];
				arrayLL[j] = temp;
			}
		}
		LinkedLists temp = arrayLL[i + 1];
		arrayLL[i + 1] = arrayLL[high];
		arrayLL[high] = temp;

		return i + 1;
	}


	/**
     * 
     * @param arrayLL  This is a linked list array (doesn't contain nulls)
     * @param low This is an integer that was 0 at main
     * @param high This is an integer value which was the linked list array length -1 in main
     * @param column This is an integer of the column of the linked list array we want to 
     * sort in alphabetic order
     * @return nothing
     */
    static void qSort(LinkedLists[] arrayLL, int low, int high, int column){

        /**
         * reference for quick sort for methods 'partition' and 'qSort':
         * https://stackoverflow.com/questions/35127804/sort-a-two-dimensional-array-by-column-using-quick-sort
         * code was borrowed and adapted as required for this assignment
         */
		if (low < high) {

			int pi = partition(arrayLL, low, high, column);

			qSort(arrayLL, low, pi - 1, column);
			qSort(arrayLL, pi + 1, high, column);
		}
	}


    /**
     * 
     * @param head This is an object of a Node class
     * @return This returns a pointer to the linked list sorted by insertion sort
     */
    public static Node insertionSort(Node head){ 

        /**
         * reference for method 'insertionSort' method:
         * https://stackoverflow.com/questions/35127804/sort-a-two-dimensional-array-by-column-using-quick-sort
         * code was borrowed and adapted as required for this assignment
         */
        Node temp = new Node(head.getData()); 
        Node prev = temp; 
        Node current = head;

        while(current != null)
        {
            if(prev.getNext() != null && prev.getData().compareTo(current.getData()) > 0){

                prev = temp;
            }

            while(prev.getNext() != null && prev.getNext().getData().compareTo(current.getData()) < 0){

                prev = prev.getNext();
            }

            Node nextNode = current.getNext();
            current.setNext(prev.getNext());
            prev.setNext(current);
            current = nextNode;
        }
        return temp.getNext();
    }


    /**
     * 
     * @param arrayLL This is linked lists array (does contain nulls)
     * @return a linked list array with the nulls removed
     */
    public static LinkedLists[] removeExtra(LinkedLists[] arrayLL){

        int arraySize = 0, p = 0;

        while((arraySize < arrayLL.length-1) && (arrayLL[arraySize] != null)){

            arraySize+=1;
        }
        LinkedLists[] newArray = new LinkedLists[arraySize];
        while(p < arraySize){

            newArray[p] = arrayLL[p];
            p+=1;
        }
        return newArray;
    }
}





class LinkedLists{
     
	Node head; 
    int linkedListSize;

	
	/**
     * LinkedLists constructor for no arguments. Sets head (object of Node) to null 
     * and linkedListSize to 0;
     * @return nothing
     */
    public LinkedLists() { 
		head = null;
        linkedListSize=0; 
	}


    /**
     * LinkedLists constructor for one argument
     * @param data This is a string value that will be used to create a new object of Node 
     * and set it to existing object of Node head.
     * @return nothing
     */
    public LinkedLists(String data){
        head = new Node(data);
        linkedListSize+=1;
    }


	/**
     * 
     * @return returns object of Node head to where it was called from.
     */
    public Node getHead() { 
		return head;
	}


    /**
     * 
     * @param head This is an object of Node
     * @return nothing
     */
    public void setHead(Node head) { 
		this.head = head;
	}


    /**
     * 
     * @param data This is a string, which will be used to create a new object of Node and 
     * add it to the end of a linked list
     * @return nothing
     */
    public void addInEnd(String data){
        Node newNode = new Node(data);
		Node current = head;
			
		while(current.getNext() != null) {
			current = current.getNext();
		}
		current.setNext(newNode);
        linkedListSize+=1;
    }


    /**
     * 
     * @return returns size of linked list
     */
    public int size() {
		return linkedListSize;
	}


    /**
     * 
     * @return return string of data 
     */
    public String toPrint(){

        StringBuffer forOutput = new StringBuffer();
        Node currElement = head;

        while(currElement.getNext() != null){

            String currElementData = currElement.getData();
            forOutput.append(currElementData + " ");
            currElement = currElement.getNext();
        }
        forOutput.append(currElement.getData());

        return forOutput.toString();
    }
}





class Node {
    
	String data;  
	Node next; 
	

	/**
     * 
     * @param value This is a string to be set to string variable 'data'
     */
    public Node(String value) {
		data = value;
		next = null;
	}


	/**
     * 
     * @param value This is a string to be set to string variable 'data'
     * @param nextValue This is an object of Node to be set to object of Node 'next'
     */
    public Node(String value, Node nextValue) {
		data = value;
		next = nextValue;
	}
	

	/**
     * 
     * @return returns a string
     */
    public String getData() { 
		return data;
	}

	
	/**
     * 
     * @param newValue This is a string value to set to be set to string variable 'data'
     */
    public void setData(String newValue) { 
		data = newValue;
	}
	

	/**
     * 
     * @return returns object of Node 'next'
     */
    public Node getNext() { 
		return next;
	}
	

	/**
     * 
     * @param nextNode This is an object of Node to be set to existing object of Node 'next'
     */
    public void setNext(Node nextNode) { 
		next = nextNode;
    }
}