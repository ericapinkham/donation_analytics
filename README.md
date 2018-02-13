# Table of Contents
1. [Introduction](README.md#introduction)
2. [Dependencies](README.md#dependencies)
3. [Main](README.md#main)
4. [Fec Package](README.md#fec)
    1. [Donation](README.md#donation)
    2. [FileIO](README.md#fileio)
    3. [Processor](README.md#processor)
    4. [Recipient](README.md#recipient)
    5. [Tracker](README.md#tracker)
5. [Trees Package](README.md#trees)
    1. [RedBlackTree](README.md#redblacktree)

# Introduction
Below is an outline of the various components of my solution. In short, the code outline below takse the following steps:
* For each line in the file
    * Convert the line to a **donation** object.
    * Validate.
    * Record information if successful.
    * If the record comes from a repeat donor:
        * Create a new **recipient** object from the repeat donor.
        * Calculation of the percentile is done on instantiation.
        * If the recipient has already received a donation from the same zip code, we aggregate.
        * Record the recipient's information in the output file.

# Dependencies
This code was run and tested using:
* **scala** version 2.12.4
* **scalatest** version 3.0.1
* **sbt** version 1.1.0

All other code uses standard scala libraries.

The computer used to write and run this code is an AMDx86_64 4Ghz with 16GB ram running Linux Mint 18.3 'Sylvia'.

# Main
This function is the main executing function. 
I.e., running this with the appropriate parameters will process the file and produce the desired output.

Parameters (in position order):
* The path to itcont.txt.
* The path to percentile.txt.
* (Optional) the path to the output file.

# FEC
## Donation
This class provides a representation of the raw data input.
It's most conveniently constructed using the companion objects apply method using a single string value.
This constructor method will produce a donation object given a string matching the FEC data dictionary guidelines.
The class has various helper methods for extracting the appropriate values from the input string.
The final important method of this class is the **isValid** method.
This returns true if various data validation tests are passed.


## FileIO
This class provides utilities for reading the input files and writing to the output.

## Processor
This trait provides abstraction for the **Main** object. Abstraction of various methods was done to facilitate testing. 

## Recipient
The **recipient** class keeps track of donations made to a recipient by zip code.
It is closely coupled with the **tracker** class.
Each time a recipient is created, the percentile is calculated through use of an object reference to the tracker class.
Additionally, the recipient class provides simple and safe ways of aggregating data through its **+** method.

## Tracker
This class keeps track of all of the important stats we are tracking.
It has the following members:
* **recipients**: A hash map of recipient objects by committee id, 5 digit zip code and year.
This facilitates aggregation of recipient objects.
* **donors**: This is another hash map which keeps track of what donors made donations in what year.
* **contributions**: This is a red-black tree which is used to keep track of the amounts of donations from repeat donor.

Additionally, this class provides two useful methods:
* **isFromRepeatDonor**: Takes a donation object and returns true if the donor made a donation in the prior year.
* **getRecipient**: Constructs a new recipient object from a donation object.

# Trees
## RedBlackTree
This class provides a purely functional implementation of a red-black tree (more specifically an order-statistics tree).
This implementation is modeled after "Red-Black Trees in a Functional Setting" by Chris Okasaki, 1993.
The idea behind implementing this is to facilitate computation of the kth order statistics. 
The advantages of using this over naively computing the value from a list of recorded values is in runtime complexity.
An order-statistics tree has the following time complexities:
* **Insert**: O(log(n))
* **kth-smallest**: O(log(n))

Since these are the only operations we are interested in, this provides an ideal method for computing order statistics with low time complexity.