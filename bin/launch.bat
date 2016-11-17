start java jade.Boot -gui
timeout 3
start java jade.Boot -container -host localhost -agents ConstantAgent:agents.AgentConstant
start java jade.Boot -container -host localhost -agents BasicAgent:agents.AgentBasicEquation
start java jade.Boot -container -host localhost -agents SummativeAgent:agents.AgentSummativeEquation
start java jade.Boot -container -host localhost -agents MultiplicativeAgent:agents.AgentMultiplicativeEquation
