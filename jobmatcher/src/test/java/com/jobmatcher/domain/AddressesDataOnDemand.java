package com.jobmatcher.domain;

import com.jobmatcher.reference.States;
import com.jobmatcher.service.AddressService;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.roo.addon.dod.RooDataOnDemand;
import org.springframework.stereotype.Component;

@Component
@Configurable
@RooDataOnDemand(entity = Addresses.class)
public class AddressesDataOnDemand {

	private Random rnd = new SecureRandom();

	private List<Addresses> data;

	@Autowired
    AddressService addressService;

	public Addresses getNewTransientAddresses(int index) {
        Addresses obj = new Addresses();
        setAddress(obj, index);
        setCity(obj, index);
        setStates(obj, index);
        setZip(obj, index);
        return obj;
    }

	public void setAddress(Addresses obj, int index) {
        String address = "address_" + index;
        if (address.length() > 255) {
            address = address.substring(0, 255);
        }
        obj.setAddress(address);
    }

	public void setCity(Addresses obj, int index) {
        String city = "city_" + index;
        if (city.length() > 45) {
            city = city.substring(0, 45);
        }
        obj.setCity(city);
    }

	public void setStates(Addresses obj, int index) {
        States states = States.class.getEnumConstants()[0];
        obj.setStates(states);
    }

	public void setZip(Addresses obj, int index) {
        String zip = "zip_" + index;
        if (zip.length() > 5) {
            zip = zip.substring(0, 5);
        }
        obj.setZip(zip);
    }

	public Addresses getSpecificAddresses(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        Addresses obj = data.get(index);
        BigInteger id = obj.getId();
        return addressService.findAddresses(id);
    }

	public Addresses getRandomAddresses() {
        init();
        Addresses obj = data.get(rnd.nextInt(data.size()));
        BigInteger id = obj.getId();
        return addressService.findAddresses(id);
    }

	public boolean modifyAddresses(Addresses obj) {
        return false;
    }

	public void init() {
        int from = 0;
        int to = 10;
        data = addressService.findAddressesEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'Addresses' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<Addresses>();
        for (int i = 0; i < 10; i++) {
            Addresses obj = getNewTransientAddresses(i);
            try {
                addressService.saveAddresses(obj);
            } catch (ConstraintViolationException e) {
                StringBuilder msg = new StringBuilder();
                for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                    ConstraintViolation<?> cv = iter.next();
                    msg.append("[").append(cv.getConstraintDescriptor()).append(":").append(cv.getMessage()).append("=").append(cv.getInvalidValue()).append("]");
                }
                throw new RuntimeException(msg.toString(), e);
            }
            data.add(obj);
        }
    }
}
