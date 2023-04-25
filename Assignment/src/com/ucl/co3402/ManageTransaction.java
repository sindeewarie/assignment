package com.ucl.co3402;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class ManageTransaction 
{
	 private static Hashtable<Integer, Item> items = new Hashtable<Integer, Item>();
     private static Hashtable<String, Employee> employees = new Hashtable<String, Employee>();
     private static List<Item> personalUsage = new ArrayList<Item>();
     private static List<TransactionLog> LogItems = new ArrayList<TransactionLog>();

     public static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
     
     public static void main(String[] args) throws Exception
     {
         InitialiseData();

         DisMenu();
         int option = Choice();

         while (option != 8)
         {
             switch (option)
             {
                 case 1:
                     Case1();
                     break;
                 case 2:
                     Case2();
                     break;
                 case 3:
                     Case3();
                     break;
                 case 4:
                     Case4();
                     break;
                 case 5:
                     Case5();
                     break;
                 case 6:
                     Case6();
                     break;
                 case 7:
                     Case7();
                     break;
                 case 8:
                     //Exit();
                     break;
             }
             DisMenu();
             option = Choice();
         }
     }

     private static void DisMenu()
     {
         System.out.println("\n1. Add new item");
         System.out.println("2. Add to stock");
         System.out.println("3. Take from stock");
         System.out.println("4. Inventory Report");
         System.out.println("5. Financial Report");
         System.out.println("6. Display Transaction Log");
         System.out.println("7. Report Personal Usage");
         System.out.println("8. Exit");
     }

     private static int Choice()
     {
         int option = ReadInteger("\nOption");
         while (option < 1 || option > 8)
         {
             System.out.println("\nChoice not recognised, Please enter again");
             option = ReadInteger("\nOption");
         }
         return option;
     }

     private static int ReadInteger(String prompt)
     {
         try
         {
        	 System.out.println(prompt + ": > ");
             return Integer.parseInt(reader.readLine().toString());
         }
         catch (Exception e)
         {
             return -1;
         }
     }

     private static float ReadFloat(String prompt)
     {
         try
         {
        	 System.out.println(prompt + ": > ");
             return Float.parseFloat(reader.readLine().toString());
         }
         catch (Exception e)
         {
             return -1;
         }
     }

     private static void InitialiseData()
     {
         AddEmployee(new Employee("Graham"));
         AddEmployee(new Employee("Phil"));
         AddEmployee(new Employee("Jan"));

         
         Item i1 = new Item(1, "Pencil", 10, "Graham", LocalDateTime.now());
         items.put(i1.ID, i1);
         CreateLogEntry("Item Added", i1, 0.25f, LocalDateTime.now());

         Item i2 = new Item(2, "Eraser", 20, "Phil", LocalDateTime.now());
         CreateLogEntry("Item Added", i2, 0.15f, LocalDateTime.now());
         items.put(i2.ID, i2);
     }

     private static void Case4()
     {
         // Inventory Report
    	 System.out.println("\nAll items");
    	 System.out.println( "ID"+ "\t" +"Name" + "\t" + "Quantity");
         for(Item i : items.values())
         {
             DisplayItem(i);
         }
     }

     private static void DisplayItem(Item i)
     {
    	 System.out.println(
    	 	 i.ID+ "\t" +
             i.Name+ "\t" +
             i.Quantity);
     }

     private static void DisplayAdd(Item i, float itemPrice)
     {
    	 System.out.println(
             i.ID + "\t" +
             i.Name + "\t" +
             itemPrice + "\t" +
             i.Quantity + "\t" +
             i.DateCreated);
    	 
    	 System.out.println();
     }

     private static void Case6()
     {
         // transaction log
         List<TransactionLog> tls = GetTransactions();

         System.out.println("\nTransaction Log:");
         System.out.println("Date"+ "\t" + "Type"+ "\t" + "ID"+ "\t" + "Name"+ "\t" + "Employee"+ "\t" + "Price");
         for(TransactionLog tl : tls)
         {
             DisTransactions(tl);
         }
     }

     private static void Case5()
     {
         // Financial report
         TotalPrice();
     }

     private static void Case7() throws IOException
     {
         // Personal usage report
         Report();
     }

     public static void Case1() throws IOException
     {
         int itemId = ReadInteger("\nItem ID");
         System.out.println("Item Name: > ");
         String itemName = reader.readLine().toString();
         int itemQuantity = ReadInteger("Item Quantity");
         float itemPrice = ReadFloat("Item Price");
         System.out.println("Employee Name : > ");
         String itemEmpName = reader.readLine().toString();
         if (itemId < 0 || itemQuantity < 0 || itemPrice < 0 || itemName == "" || itemEmpName == "")
         {
        	 System.out.println("ERROR: ID, Quantity or Price below 0 or Item name/ Employee name is empty");
         }
         else
         {
             if (NumItemsInStock() < 1)
             {
                 Item i = new Item(itemId, itemName, itemQuantity, itemEmpName, LocalDateTime.now());
                 items.put(i.ID, i);
                 CreateLogEntry("Item Added", i, itemPrice, LocalDateTime.now());

                 System.out.println("Stock Added: ");
                 System.out.println("ID"+ "\t" + "Name"+ "\t" + "Price"+ "\t" + "Quantity"+ "\t" + "Date Added");
                 for (Item item : items.values())
                 {
                     DisplayAdd(item, itemPrice);
                 }
             }
             else
             {
                 try
                 {
                     Item temp = items.get(itemId);
                     System.out.println("Item is already in stock.");
                 }
                 catch (Exception e)
                 {
                     Item i = new Item(itemId, itemName, itemQuantity, itemEmpName, LocalDateTime.now());
                     items.put(i.ID, i);
                     CreateLogEntry("Item Added", i, itemPrice, LocalDateTime.now());

                     System.out.println("Stock Added: ");
                     System.out.println( "ID"+ "\t" + "Name"+ "\t" + "Price"+ "\t" + "Quantity"+ "\t" + "Date Added");
                     for(Item item : items.values())
                     {
                         DisplayAdd(item, itemPrice);
                     }
                 }
             }
         }
     }

     public static void Case2() throws Exception
     {
         int itemId = ReadInteger("\nItem ID");
         Item temp;
         try
         {
             temp = FindItem(itemId);
         }
         catch (Exception e)
         {
             throw new Exception("ERROR: Item not found");
         }
         float itemPrice = ReadFloat("Item Price");
         System.out.println("Employee Name : > ");
         String itemEmpName = reader.readLine().toString();
         int itemQuantity = ReadInteger("How many items would you like to add?");
         if (itemQuantity < 0 || itemEmpName == "")
         {
        	 System.out.println("ERROR: Quantity being added is below 0 or Employee name is empty");
         }
         else
         {
             FindItem(itemId).Quantity += itemQuantity;
             System.out.println(itemQuantity + " items have been added to Item ID: " + itemId + " on " + LocalDateTime.now());
             Item i = new Item(itemId, temp.Name, itemQuantity, itemEmpName, LocalDateTime.now());
             CreateLogEntry("Stock Updated", i, itemPrice, LocalDateTime.now());
         }
     }

     public static void Case3() throws Exception
     {
    	 System.out.println("Employee Name: > ");
         String empname = reader.readLine().toString();
         try
         {
             FindEmployee(empname);

         }
         catch (Exception e)
         {
             throw new Exception("ERROR: Employee not found");
         }
         int itemId = ReadInteger("\nItem ID");
         Item temp;
         try
         {
             temp = FindItem(itemId);
         }
         catch (Exception e)
         {
             throw new Exception("ERROR: Item not found");
         }
         int itemQuantity = ReadInteger("How many items would you like to remove?");
         if (itemQuantity > temp.Quantity || itemQuantity < 0)
         {
        	 System.out.println("ERROR: Quantity too many or below 0");
             //return false;
         }
         else
         {
             FindItem(itemId).Quantity -= itemQuantity;
             System.out.println(empname + " has removed " + itemQuantity + " of Item ID: " + itemId + " on " + LocalDateTime.now());
             Item i = new Item(itemId, temp.Name, itemQuantity, empname, LocalDateTime.now());
             CreateLogEntry("Item Removed", i, -1, LocalDateTime.now());
             CreateUsageEntry(itemId, temp.Name, itemQuantity, empname, LocalDateTime.now());
             //return true;
         }
     }

     public static Item FindItem(int itemId) throws Exception
     {
         try
         {
             return items.get(itemId);
         }
         catch (Exception e)
         {
             throw new Exception("ERROR: Item not found");
         }
     }

     public static void TotalPrice()
     {
         float total = 0;

         Hashtable<Integer, Item> DictItems = (Hashtable<Integer, Item>) items.values();

         System.out.println("Financial Report:");

         for (TransactionLog entry : LogItems)
         {
             if (entry.TypeOfTransaction.equals("Item Added") 
                 || entry.TypeOfTransaction.equals("Stock Updated"))
             {
                 float cost = entry.ItemPrice * entry.ItemQuantity;
                 System.out.println(entry.ItemName + "\t" + cost);
                 total += cost;
             }
         }

         System.out.println("Total price of all items:"+ "\t" +total);
     }

     public static int NumItemsInStock()
     {
         return items.size();
     }

     public static Employee FindEmployee(String EmpName) throws Exception
     {
         try
         {
             return employees.get(EmpName);
         }
         catch (Exception e)
         {
             throw new Exception("ERROR: Employee not found");
         }
     }

     public static void AddEmployee(Employee e)
     {
         employees.put(e.EmpName, e);
     }

     public static void CreateUsageEntry(int id, String name, int quantity, String empName, LocalDateTime dateCreated)
     {
         personalUsage.add(new Item(id, name, quantity, empName, dateCreated));
     }

     public static void CreateLogEntry(String type, Item i, float itemPrice, LocalDateTime dateAdded)
     {
         LogItems.add(new TransactionLog(type, i, itemPrice, dateAdded));
     }

     public static void DisTransactions(TransactionLog tl)
     {
         System.out.println(
         tl.DateAdded + "\t" +
         tl.TypeOfTransaction + "\t" +
         tl.ItemID + "\t" +
         tl.ItemName + "\t" +
         tl.EmployeeName + "\t" +
         (tl.TypeOfTransaction.equals("Item Removed") ? "" : "" + tl.ItemPrice));
     }

     public static List<TransactionLog> GetTransactions()
     {
         List<TransactionLog> list = new ArrayList<TransactionLog>();

         for(TransactionLog tl : LogItems)
         {
             list.add(tl);
         }

         return list;
     }

     public static void Report() throws IOException
     {
    	 System.out.println("Enter employee name : > ");
         String empname = reader.readLine().toString();

         System.out.println("Date Taken"+ "\t" + "ID"+ "\t" + "Name"+ "\t" + "Quantity");

         for(TransactionLog logItem : LogItems)
         {
             if (logItem.TypeOfTransaction.equals("Item Removed") && logItem.EmployeeName == empname)
             {
                 DisPersonalUsage(logItem.DateAdded, logItem.ItemID, logItem.ItemName, logItem.ItemQuantity);
             }
         }
     }

     public static void DisPersonalUsage(LocalDateTime date, int id, String name, int quantity)
     {
    	 System.out.println(date + "\t" + id + "\t" + name + "\t" + quantity);
     }

}
