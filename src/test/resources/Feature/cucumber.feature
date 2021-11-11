Feature: New Customer Register

//@ignore
Scenario: Verification of New Customer Registration  
When Register as name 'Krishna' with email 'krishna@abc.com',mobile '6578009430' and password 'krishna'
Then Success

Scenario: Verification of Get Customers 
When Ask for all customers
Then Return all customers

Scenario: Verification of get Customer by email  
Given  A customer email as 'krishna1@abc.com'
Then Return the said customer

Scenario: Verification of Customer by wrong email  
Given  A customer with wrong email as 'doesnotexist@abc.com'
Then No customer should return

Scenario: Verification of delete Customer   
Given  Earlier customer delete the same
Then  Delete successful





