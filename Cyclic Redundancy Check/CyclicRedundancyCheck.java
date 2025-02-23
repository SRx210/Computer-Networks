import java.util.Scanner;

public class CyclicRedundancyCheck
{
	// These below variables are declared global within the class for ease of accessing the variables in multiple functions
	static StringBuilder gx;
	static StringBuilder data;
	static StringBuilder old_data;
	static String remainder;
	static int k, n;
	
	static void User_Inputs()
	{
		Scanner s = new Scanner(System.in);
		
		System.out.println("\nError Detection Algorithm: Cyclic Redundancy Check (CRC)");
		System.out.print("Enter generator polynomial (in bits): ");
		gx = new StringBuilder(s.nextLine());
		k = gx.length();
		
		System.out.print("Enter data (in bits): ");
		data = new StringBuilder(s.nextLine());
		n = data.length();

		old_data = new StringBuilder(data);
		s.close();
	}
	
	static StringBuilder RepeatZeroes()
	{
		for(int i = 0; i < (k - 1); i++)
		{
			data.append('0');
		}
		return data;
	}
	
	static void XOR(StringBuilder dividend, int current_idx)
	{
		for(int i = 0; i < k; i++)
		{
			dividend.setCharAt(current_idx + i, (dividend.charAt(current_idx + i) == gx.charAt(i)) ? '0' : '1');	// Performing XOR Operation between the dividend and g(x)
		}
	}
		
	static void CRC_Sender()
	{
		System.out.println("\nPerforming Cyclic Redundancy Check (CRC) at Sender's Side....");
		StringBuilder dividend = new StringBuilder(data);
		int current_idx = 0;
		while(current_idx <= (dividend.length() - k))
		{
			if(dividend.charAt(current_idx) == '1')
			{
				XOR(dividend, current_idx);
			}
			current_idx++;
		}
		remainder = dividend.substring(n, (n + k - 1));
		System.out.println("CodeWord = " + old_data.toString() + " " + remainder);
		System.out.println("Actual Data = " + old_data.toString());
		System.out.println("CRC Code = " + remainder);
		System.out.println("Now Transmitting the CodeWord to Receiver's Side");
		
	}
	
	static void CRC_Receiver()
	{
		System.out.println("\nPerforming Cyclic Redundancy Check (CRC) at Receiver's Side....");
		StringBuilder CodeWord = new StringBuilder(old_data.toString() + remainder);
		int current_idx = 0;
		while(current_idx <= (CodeWord.length() - k))
		{
			if(CodeWord.charAt(current_idx) == '1')
			{
				XOR(CodeWord, current_idx);
			}
			current_idx++;
		}
		if(CodeWord.toString().matches("0+"))
		{
			System.out.println("No Error Detected at Receiver's Side!!\n");
		}
		else
		{
			System.out.println("Error Detected at Receiver's Side!!\n");
		}
	}
	
	static void CRC_Receiver_With_Error()	// This functions is specially made to detect error
	{
		System.out.println("\nPerforming Cyclic Redundancy Check (CRC) at Receiver's Side (WITH ERROR)....");
        StringBuilder CodeWord = new StringBuilder(old_data.toString() + remainder);

        // Introduce an error at bit position 3 (this is done because in general due to noise the bit changes)
        if (CodeWord.length() > 2) 
		{
            CodeWord.setCharAt(2, (CodeWord.charAt(2) == '0') ? '1' : '0');
            System.out.println("Simulated Error Introduced: Codeword is now " + CodeWord);
        } 
		else 
		{
            System.out.println("Codeword too short to introduce error at index 2");
            return; // Exit early if we can't introduce the error.
        }

        int current_idx = 0;
        while (current_idx <= (CodeWord.length() - k)) 
		{
            if (CodeWord.charAt(current_idx) == '1') 
			{
                XOR(CodeWord, current_idx);
            }
            current_idx++;
        }

        if (CodeWord.toString().matches("0+")) 
		{
            System.out.println("No Error Detected at Receiver's Side!! (This should NOT happen with the error)");
        } 
		else 
		{
            System.out.println("Error Detected at Receiver's Side!!\n");
        }
    }
	
	public static void main(String[] args)
	{
		User_Inputs();	// Get User Inputs (Data and G(x)) from User
		RepeatZeroes();	// Append (k-1) zeroes to the "data" => "updated_data"
		CRC_Sender();	// Perform CRC at Sender's Side (Generating Actual Data and CRC Code)
		CRC_Receiver();	// Perform CRC at Receiver's Side (Verification)
		CRC_Receiver_With_Error(); // Perform CRC (with error) at Receiver's Side
	}
}
	
