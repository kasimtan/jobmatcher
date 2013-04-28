package com.jobmatcher.domain;

import com.jobmatcher.service.AddressService;
import java.math.BigInteger;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.roo.addon.test.RooIntegrationTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext*.xml")
@Configurable
@RooIntegrationTest(entity = Addresses.class, transactional = false)
public class AddressesIntegrationTest {

    @Test
    public void testMarkerMethod() {
    }

	@Autowired
    AddressesDataOnDemand dod;

	@Autowired
    AddressService addressService;

	@Test
    public void testCountAllAddresseses() {
        Assert.assertNotNull("Data on demand for 'Addresses' failed to initialize correctly", dod.getRandomAddresses());
        long count = addressService.countAllAddresseses();
        Assert.assertTrue("Counter for 'Addresses' incorrectly reported there were no entries", count > 0);
    }

	@Test
    public void testFindAddresses() {
        Addresses obj = dod.getRandomAddresses();
        Assert.assertNotNull("Data on demand for 'Addresses' failed to initialize correctly", obj);
        BigInteger id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Addresses' failed to provide an identifier", id);
        obj = addressService.findAddresses(id);
        Assert.assertNotNull("Find method for 'Addresses' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'Addresses' returned the incorrect identifier", id, obj.getId());
    }

	@Test
    public void testFindAllAddresseses() {
        Assert.assertNotNull("Data on demand for 'Addresses' failed to initialize correctly", dod.getRandomAddresses());
        long count = addressService.countAllAddresseses();
        Assert.assertTrue("Too expensive to perform a find all test for 'Addresses', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<Addresses> result = addressService.findAllAddresseses();
        Assert.assertNotNull("Find all method for 'Addresses' illegally returned null", result);
        Assert.assertTrue("Find all method for 'Addresses' failed to return any data", result.size() > 0);
    }

	@Test
    public void testFindAddressesEntries() {
        Assert.assertNotNull("Data on demand for 'Addresses' failed to initialize correctly", dod.getRandomAddresses());
        long count = addressService.countAllAddresseses();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<Addresses> result = addressService.findAddressesEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'Addresses' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'Addresses' returned an incorrect number of entries", count, result.size());
    }

	@Test
    public void testSaveAddresses() {
        Assert.assertNotNull("Data on demand for 'Addresses' failed to initialize correctly", dod.getRandomAddresses());
        Addresses obj = dod.getNewTransientAddresses(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'Addresses' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'Addresses' identifier to be null", obj.getId());
        addressService.saveAddresses(obj);
        Assert.assertNotNull("Expected 'Addresses' identifier to no longer be null", obj.getId());
    }

	@Test
    public void testDeleteAddresses() {
        Addresses obj = dod.getRandomAddresses();
        Assert.assertNotNull("Data on demand for 'Addresses' failed to initialize correctly", obj);
        BigInteger id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Addresses' failed to provide an identifier", id);
        obj = addressService.findAddresses(id);
        addressService.deleteAddresses(obj);
        Assert.assertNull("Failed to remove 'Addresses' with identifier '" + id + "'", addressService.findAddresses(id));
    }
}
