# Parallel State Machine Replication Simulator

This project aims create a simple simulator to compare 3 different ways of handling with State Machine Simulator.
1 - Without parallelism [4].
2 - With parallelism with CBASE [1].
3 - With parallelism with Batch operations [2][3].

## How to compile 

To compile this code you need the following command:

```
 mvn clean install -DskipTests
```
It needs Java 8+ and maven to compile.

## How to run
In this simulator, the executions were performed using Junit5 + maven.
It is intentional to organize and documentate the executions and to mantain the src code focused only in parallel execution and simulation. 
Every scheduler has a test class with execution. To execute execute it, just use one of following command:

```
mvn test -Dtest=SequentialSchedulerTest#schedulerTestingWithoutParallelOperations
mvn test -Dtest=CbaseSchedulerTest#schedulerTestingWithParallelOperations
mvn test -Dtest=BatchSchedulerTest#schedulerTestingWithParallelOperations
```

It will create the scheduler, the commands, start the timers and print the log in the end.
To change any configuration, just edit the config object that is create beforehand.

If the filename is present, but the file is absent, the system will create one and save. 
If the filename method is removed, then the system will always create a new set of commands.

# References and links
[1] Kotla, R., & Dahlin, M. (2004). High throughput Byzantine fault tolerance. International Conference on Dependable Systems and Networks, 2004. doi:10.1109/dsn.2004.1311928
[2] Mendizabal, O. M., Moura, R. S. T. D., Dotti, F. L., & Pedone, F. (2017). Efficient and Deterministic Scheduling for Parallel State Machine Replication. 2017 IEEE International Parallel and Distributed Processing Symposium (IPDPS). doi:10.1109/ipdps.2017.29
[3] Mendizabal, O. M. "Fast recovery in parallel state machine replication." (2016).
[4] Schneider, F. B., “Implementing fault-tolerant services using the state machine approach: A tutorial,” ACM Computing Surveys, vol. 22, no. 4, pp. 299–319, 1990.
[5] Intorruk, S., & Numnonda, T. (2019). A Comparative Study on Performance and Resource Utilization of Real-time Distributed Messaging Systems for Big Data. 2019 20th IEEE/ACIS International Conference on Software Engineering, Artificial Intelligence, Networking and Parallel/Distributed Computing (SNPD).
[6] Nilsson, E., & Pregén, V. (2020). Performance evaluation of message-oriented middleware.
