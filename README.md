<!---
 Licensed to the Apache Software Foundation (ASF) under one or more
 contributor license agreements.  See the NOTICE file distributed with
 this work for additional information regarding copyright ownership.
 The ASF licenses this file to You under the Apache License, Version 2.0
 (the "License"); you may not use this file except in compliance with
 the License.  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
-->

## Paxos 

This repository provides an implementation of the Paxos core algorithm in the form of a collection of composable  classes.

At the very least, a fundamental familiarity with Paxos is required in order to make effective use of this module. 
The algorithm necessitates compliance with a number of messaging rules as well as the saving of the state to persistent media at particular junctures throughout the protocol. 
These classes rely on the enclosing application to supply the necessary behavior and to provide solutions for the implementation-defined aspects of the protocol. 
For example, the mechanism to ensure forward progress is one of those implementation-defined aspects. 

The flexibility offered by this minimalist approach trumps that of solutions that come with a greater number of features. 
These classes make no assumptions about the application's operational environment or the message handling semantics, 
and they do not depend on any external dependencies to function properly. 
All that is required of them is to correctly implement the core algorithm within a neat little black box that can be utilized as a foundational building block for distributed applications.

This program implements a Paxos voting protocol for Suburban Council President selection that is fault tolerant and resillient to various failure types.
This application allows to work con currently when two councillors send voting proposals at the same time.
This also works when all the members in councils have immediate responses to voting queries. 
Moreover this application has gone through test cases to minimize errors that happens when an a system is made implementing distributive features.

This application is an Apache 2.0-licensed, open-source, distributed neural net library written in Java and Paxos algorithm.

## Main Features
- Members simultaneously sending proposals
- Members simultaneously accepting proposals
- Selecting majority vote as Councillor using the given criteria

## How to run
- Move to your project root folder in command line - cd ~/Project
- mvn compile
- mvn exec:java -VotingDemo.mainClass=com.mycompany.PaxosVoting;



