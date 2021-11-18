package com.alvarolongueira.managerservice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.alvarolongueira.managerservice.controller.request.customer.CreateCustomerRequest;
import com.alvarolongueira.managerservice.controller.request.customer.UpdateCustomerRequest;
import com.alvarolongueira.managerservice.domain.Customer;
import com.alvarolongueira.managerservice.exception.customer.CustomerAlreadyExistsException;
import com.alvarolongueira.managerservice.exception.customer.CustomerNotFoundException;
import com.alvarolongueira.managerservice.exception.customer.CustomerRequiredFieldsException;
import com.alvarolongueira.managerservice.repository.CustomerRepository;
import com.alvarolongueira.managerservice.repository.entity.CustomerEntity;

@Service
public class CustomerService {

	private CustomerRepository repository;

	public CustomerService(CustomerRepository repository) {
		this.repository = repository;
	}

	public List<Customer> getAllCustomers() {
		List<Customer> customers = new ArrayList<Customer>();
		this.repository.findAll().forEach(value -> customers.add(CustomerEntity.convertToDomain(value)));
		return customers;
	}

	public Customer getCustomerById(long customerId) throws CustomerNotFoundException {
		return repository.findById(customerId).map(CustomerEntity::convertToDomain)
				.orElseThrow(() -> new CustomerNotFoundException("Customer with id " + customerId + " not found"));
	}

	public Customer createCustomer(CreateCustomerRequest request) throws CustomerAlreadyExistsException, CustomerRequiredFieldsException {

		if (request.getName().isEmpty() || request.getSurname().isEmpty() || request.getPhoto() <= 0L) {
			throw new CustomerRequiredFieldsException("Customer " + request.getName() + " " + request.getSurname() + " already exists");
		}

		Optional<CustomerEntity> oldEntity = repository.findByNameAndSurname(request.getName(), request.getSurname());
		if (oldEntity.isPresent()) {
			throw new CustomerAlreadyExistsException("Customer " + request.getName() + " " + request.getSurname() + " already exists");
		}

		CustomerEntity newEntity = new CustomerEntity();
		newEntity.setName(request.getName());
		newEntity.setSurname(request.getSurname());
		newEntity.setPhoto(request.getPhoto());

		CustomerEntity entity = repository.save(newEntity);
		return entity.toDomain();
	}

	public Void deleteCustomer(long customerId) throws CustomerNotFoundException {
		Optional<CustomerEntity> oldEntity = repository.findById(customerId);
		if (!oldEntity.isPresent()) {
			throw new CustomerNotFoundException("Customer with id " + customerId + " not found");
		}

		this.repository.deleteById(customerId);
		return null;
	}

	public Customer updateCustomer(long customerId, UpdateCustomerRequest request)
			throws CustomerNotFoundException, CustomerAlreadyExistsException, CustomerRequiredFieldsException {
		if (request.getName().isEmpty() || request.getSurname().isEmpty() || request.getPhoto() <= 0L) {
			throw new CustomerRequiredFieldsException("Customer " + request.getName() + " " + request.getSurname() + " already exists");
		}

		Optional<CustomerEntity> oldEntity = repository.findById(customerId);
		if (!oldEntity.isPresent()) {
			throw new CustomerNotFoundException("Customer with id " + customerId + " not found");
		}

		Optional<CustomerEntity> oldEntitySameFields = repository.findByNameAndSurname(request.getName(), request.getSurname());
		if (oldEntitySameFields.isPresent()) {
			throw new CustomerAlreadyExistsException("Customer " + request.getName() + " " + request.getSurname() + " already exists");
		}

		CustomerEntity newEntity = new CustomerEntity();
		newEntity.setName(request.getName());
		newEntity.setSurname(request.getSurname());
		newEntity.setPhoto(request.getPhoto());
		CustomerEntity entity = repository.save(newEntity);
		return entity.toDomain();
	}

}
