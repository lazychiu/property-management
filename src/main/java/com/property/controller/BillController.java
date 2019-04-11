package com.property.controller;

import com.property.controller.result.Result;
import com.property.pojo.Bill;
import com.property.pojo.Property;
import com.property.service.BillService;
import com.property.service.PropertyService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Chichiu Yeung
 * Created in 2019/4/9 23:36
 */
@RestController
@RequestMapping("/bill")
public class BillController {

    private final PropertyService propertyService;
    private final BillService billService;

    public BillController(PropertyService propertyService, BillService billService) {
        this.propertyService = propertyService;
        this.billService = billService;
    }

    @PostMapping("/initiate")
    public Result initiate(String name, String fee, String residenceId) {
        List<Property> properties = propertyService.getAllPropertiesByResidenceId(Integer.parseInt(residenceId));
        List<Bill> list = new ArrayList<>();
        for (Property property : properties) {
            Bill bill = new Bill();
            bill.setProperty(property.getId().toString());
            bill.setName(name);
            bill.setFee(fee);
            list.add(bill);
        }
        billService.insertBills(list);
        return Result.success("发起缴费成功");
    }

    @GetMapping("/getAllBills")
    public Result getAllBills() {
        return Result.success().add("bills", billService.getAllBills());
    }

    @GetMapping("/getBillDetails")
    public Result getBillDetails(String name) {
        return Result.success().add("details", billService.getBillDetailByName(name));
    }

    @GetMapping("/getBacklogByPropertyId")
    public Result getBacklogByPropertyId(Integer propertyId) {
        return Result.success().add("backlog", billService.getBillsByPropertyId(propertyId));
    }

    @GetMapping("/getPaidBillsByPropertyId")
    public Result getPaidBillsByPropertyId(Integer propertyId) {
        return Result.success().add("paidBills", billService.getPaidBillsByPropertyId(propertyId));
    }

    @PostMapping("/pay")
    public Result pay(@RequestBody Bill bill) {
        billService.payBill(bill);
        return Result.success("缴费成功").add("bill", billService.getBillById(bill.getId()));
    }
}
