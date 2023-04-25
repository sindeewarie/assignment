package com.ucl.co3402;

import java.time.LocalDateTime;

public class Item 
{
	public int ID;
    public String Name;
    public int Quantity;
    public String EmpName;
    public LocalDateTime DateCreated;
    
    public int getID()
    {
    	return ID;
    }
    
    public String getName()
    {
    	return Name;
    }
    
    public int Quantity()
    {
    	return Quantity;
    }
    
    public String EmpName()
    {
    	return EmpName;
    }
    
    public LocalDateTime getDateCreated()
    {
    	return DateCreated;
    }
    
    public void setQuantity(int quantity)
    {
    	this.Quantity = quantity;
    }
    
    public Item(int id, String name, int quantity, String empName, LocalDateTime dateCreated)
    {
        this.ID = id;
        this.Name = name;
        this.Quantity = quantity;
        this.EmpName = empName;
        this.DateCreated = dateCreated;
    }
}
