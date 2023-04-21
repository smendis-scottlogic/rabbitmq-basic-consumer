# Getting Started

### Prerequisites
* Docker Desktop
* Java SDK 17

### Docker Container Commands

To create a new docker container run

`docker-compose -p "rabbitmq-container" up`

To restart an existing container

`docker-compose -p "rabbitmq-container" start`

To stop a container

`docker-compose -p "rabbitmq-container" stop`

To destroy an existing container

`docker-compose -p "rabbitmq-container" down`

### Management Console

* URL: [http://localhost:15672/](http://localhost:15672/)
* Username: guest
* Password: guest

