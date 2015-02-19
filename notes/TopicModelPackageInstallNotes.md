Installing the R TopicModels package

---------------------------------------------------

### R Setup

Installing R (Ubuntu):

	- Go here to download R http://cran.rstudio.com/
		- Use sudo nano /etc/apt/sources.list to add the support:
			- deb http://cran.rstudio.com/bin/linux/ubuntu trusty/


---------------------------------------------------

### TopicModels Package Setup

Installing topicmodels
	
	- Install the "tm" package (install.packages("tm"))
	- Install gsl
		- Follow these instructions (http://stackoverflow.com/questions/24172188/how-can-i-install-topicmodels-package-in-r)
	- Install the topicmodels package (install.packages("topicmodels"))


---------------------------------------------------

### MySQL Setup

Install mysql-server-5.5 / mysql-server

	- apt-get update 
	- apt-get upgrade
	- might have to remove and clean

Install mysql-workbench
	
Install libmysqlclient-dev (mySQL dev version)
	
	- https://launchpad.net/ubuntu/trusty/+package/libmysqlclient-dev

Install RMySQL


---------------------------------------------------

### Groovy SQL Setup

Install mysql connector (jdbc)

    - Place in a lib and add to the classpath (Library)
    - Groovy should have a Sql Module ready for use
    - Url to DB would be:
        - 'jdbc:mysql://localhost:3306/WebMD1'
        - Use user and password

