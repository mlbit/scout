# Architecture Vision
### Record keeping rewards
    - File storage : data will be store in memory & disk. The newest or most active records are the records that will be store in memory, whereas the other data will be store in disk.
    - Memory storage : 
### Nodes
    There are two types of nodes:
    - Regular Node - these nodes are responsible to transmit or pass transaction to other nodes
    - Blockers Node - these nodes are responsible to create blocks that store the ledger data
### Blocker 
    #### Goal of the blockers
         1. Transaction should be recorded in the blockchain within 1sec
         2. Blocks should be created every 100ms if any transaction
    Node can be selected to be play as one of the blockers that generate the block. This would allow 
    to control the number of blockers that would maintain the network performance. The network has to maintain
    the maximum 100ms blocking time.
    To maintain this response time, the number of blockers would scale according to the number of transaction.

