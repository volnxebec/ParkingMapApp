/** Steve
 * This is the class we used to store each parking lots information
 */
package com.example.wind.myapplication;

public class ParkingLot
{
	private String address;
	private String half_hour_rate;
	private String capacity;
	private String max_height;

	public ParkingLot (String address, String half_hour_rate, String capacity, String max_height)
	{
		this.address = address;
		this.half_hour_rate = half_hour_rate;
		this.capacity = capacity;
		this.max_height = max_height;
	}

	public String getAddress()
	{
		return address;
	}
	public String getHalfHourRate()
	{
		return half_hour_rate;
	}
	public String getCapacity()
	{
		return capacity;
	}
	public String getMaxHeight()
	{
		return max_height;
	}
}