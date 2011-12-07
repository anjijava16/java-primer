package com.slieer.ejbpro.ejb.webservice;

import javax.ejb.Stateless;
import javax.jws.WebService;

import com.slieer.ejbpro.ifc.Car;


@Stateless
@WebService(name = "CarWebService")
public class CarWebService {

	public Car callCar(String name) {
		System.out.println("客户" + name + "端调用了服务器端的代码");

		Car car = new Car();
		car.setName("宝马");
		car.setMessage("BMW");

		return car;
	}
}
