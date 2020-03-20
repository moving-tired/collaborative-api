default: build

docker_project = voucher-service

docker_compose_dev = docker-compose \
			 -p $(docker_project) \
			 -f $(shell pwd)/docker-compose-local.yml

build:
	./gradlew clean build

start-devenv:
	$(docker_compose_dev) up -d --build --no-recreate
	sleep 10s # Giving time to all instances are up and running before population
	./local-env/populate/sqs.sh
	./local-env/populate/s3.sh

clear-devenv:
	$(docker_compose_dev) down -v --remove-orphans
