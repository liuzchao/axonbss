package com.ai.bss.webui.customer.controller;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ai.bss.api.customer.CustomerId;
import com.ai.bss.api.customer.command.CreateCustomerFromPartyCommand;
import com.ai.bss.api.customer.command.CreateIndividualCustomerCommand;
import com.ai.bss.api.customer.command.CreateLegalCustomerCommand;
import com.ai.bss.api.party.PartyId;
import com.ai.bss.mutitanent.TenantContext;
import com.ai.bss.query.api.customer.CustomerEntry;
import com.ai.bss.query.api.party.IndividualEntry;
import com.ai.bss.query.api.party.LegalOrganizationEntry;
import com.ai.bss.query.api.party.PartyEntry;
import com.ai.bss.webui.customer.model.Customer;
import com.ai.bss.webui.customer.model.IndividualCustomer;
import com.ai.bss.webui.customer.model.LegalCustomer;
import com.ai.bss.webui.util.BaseController;

@Controller
@RequestMapping("/customer")
public class CustomerController  extends BaseController{

    @RequestMapping(method = RequestMethod.GET)
    public String get(Model model) {
        model.addAttribute("items", client.getForObject("http://customer-query-service/customer",Iterable.class));
        //model.addAttribute("items", partyRepository.findByType("INDIVIDUAL"));
        return "customer/list";
    }

    @RequestMapping(value = "/{customerId}", method = RequestMethod.GET)
    public String details(@PathVariable String customerId, Model model) {
        CustomerEntry customerEntry = client.getForObject("http://customer-query-service/customer/"+customerId,CustomerEntry.class);
        if(null!=customerEntry){        	
        	if (customerEntry.getParty() instanceof IndividualEntry){
        		IndividualEntry individual=(IndividualEntry)customerEntry.getParty();
        		IndividualCustomer customer=new IndividualCustomer();
        		customer.setFirstName(individual.getFirstName());
        		customer.setLastName(individual.getLastName());
        		customer.setCustomerId(customerId);
        		customer.setPartyId(customerEntry.getParty().getPartyId());
        		customer.setCustomerName(customerEntry.getCustomerName());
        		customer.setCustSegmentId(customerEntry.getCustSegmentId());
        		customer.setServiceCode(customerEntry.getServiceCode());
        		customer.setServiceLevel(customerEntry.getServiceLevel());
        		customer.setCreateDate(customerEntry.getCreateDate());
        		customer.setState(customerEntry.getState());                           
        		model.addAttribute("individualCustomer", customer);
        		return "customer/individualCustomerDetail";
        	}else{
        		LegalOrganizationEntry legal=(LegalOrganizationEntry)customerEntry.getParty();
        		LegalCustomer customer=new LegalCustomer();
        		customer.setLegalName(legal.getLegalName());
        		if(null!=legal.getParentLegalOrganization()){
        			customer.setParentLegalId(legal.getPartyId());
            		customer.setParentLegalName(legal.getParentLegalOrganization().getLegalName());
        		}       		
        		customer.setCustomerId(customerId);
        		customer.setPartyId(customerEntry.getParty().getPartyId());
        		customer.setCustomerName(customerEntry.getCustomerName());
        		customer.setCustSegmentId(customerEntry.getCustSegmentId());
        		customer.setServiceCode(customerEntry.getServiceCode());
        		customer.setServiceLevel(customerEntry.getServiceLevel());
        		customer.setCreateDate(customerEntry.getCreateDate());
        		customer.setState(customerEntry.getState());                           
        		model.addAttribute("legalCustomer", customer);
        		return "customer/legalCustomerDetail";
        	}
    		
        
        }
        return "customer";
    }
    
    @RequestMapping(value = "/createCustomerFromParty/{partyId}", method = RequestMethod.GET)
    public String createCustomerFromPartyForm(@PathVariable String partyId,Model model) {
    	Customer customer=new Customer();
    	customer.setPartyId(partyId);
    	PartyEntry partyEntry = client.getForObject("http://party-query-service/party/"+partyId,PartyEntry.class);
    	customer.setCustomerName(partyEntry.getName());
        model.addAttribute("customer", customer);        
        return "customer/createCustomerFromParty";
    }
    
    @RequestMapping(value = "/createIndividualCustomer", method = RequestMethod.GET)
    public String createIndividualCustomerForm(Model model) {
    	IndividualCustomer customer=new IndividualCustomer();    	   	
        model.addAttribute("individualCustomer", customer);        
        return "customer/createIndividualCustomer";
    }
    
    @RequestMapping(value = "/createLegalCustomer", method = RequestMethod.GET)
    public String createLegalCustomerForm(Model model) {
    	LegalCustomer customer=new LegalCustomer();    	   	
        model.addAttribute("legalCustomer", customer);        
        return "customer/createLegalCustomer";
    }
    
    @RequestMapping(value = "/createCustomerFromParty/createCustomerFromParty", method = RequestMethod.POST)
    public String createCustomerFromParty(@ModelAttribute("customer") @Valid Customer customer, BindingResult bindingResult, Model model) {
    	if (!bindingResult.hasErrors()) {
    		CustomerId customerId=new CustomerId();
    		PartyId partyId=new PartyId(customer.getPartyId());
    		CreateCustomerFromPartyCommand command =new CreateCustomerFromPartyCommand(customerId,partyId);
    		command.setServiceCode(customer.getServiceCode());
    		command.setServicePassword(customer.getServicePassword());
    		command.setTenantId(TenantContext.getCurrentTenant());
    		command=client.postForObject("http://customer-service/createCustomerFromPartyCommand",command,CreateCustomerFromPartyCommand.class);
    		return "redirect:/customer";
    	}
    	return "createCustomerFromParty";
    }
    
    @RequestMapping(value = "/createIndividualCustomer", method = RequestMethod.POST)
    public String createIndividualCustomer(@ModelAttribute("individualCustomer") @Valid IndividualCustomer customer, BindingResult bindingResult, Model model) {
    	if (!bindingResult.hasErrors()) {
    		CustomerId customerId=new CustomerId();
    		CreateIndividualCustomerCommand command =new CreateIndividualCustomerCommand(customerId);
    		command.setFirstName(customer.getFirstName());
    		command.setLastName(customer.getLastName());
    		command.setServiceCode(customer.getServiceCode());
    		command.setServicePassword(customer.getServicePassword());   		
    		command=client.postForObject("http://customer-service/createIndividualCustomerCommand",command,CreateIndividualCustomerCommand.class);
    		return "redirect:/customer";
    	}
    	return "createIndividualCustomer";
    }
    
    @RequestMapping(value = "/createLegalCustomer", method = RequestMethod.POST)
    public String createLegalCustomer(@ModelAttribute("legalCustomer") @Valid LegalCustomer customer, BindingResult bindingResult, Model model) {
    	if (!bindingResult.hasErrors()) {
    		CustomerId customerId=new CustomerId();
    		CreateLegalCustomerCommand command =new CreateLegalCustomerCommand(customerId,customer.getLegalName());
    		command.setParentLegalId(customer.getParentLegalId());
    		command.setServiceCode(customer.getServiceCode());
    		command.setServicePassword(customer.getServicePassword());   		
    		command=client.postForObject("http://customer-service/createLegalCustomerCommand",command,CreateLegalCustomerCommand.class);
    		return "redirect:/customer";
    	}
    	return "createLegalCustomer";
    }
}
