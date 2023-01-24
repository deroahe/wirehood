<!DOCTYPE html>
<html lang="en">

<body>
    <h1 style="text-align: center">wirehood</h1>
    <div>
        <p style="text-align: center">a <i>spring boot microservices project</i></p>
        <p>wirehood is an infrastructure to build microservices upon</p>
    </div>
    <div>
        <ul> 
            <h3>discovery-server</h3>
            <li>a Eureka server (spring-cloud-starter-netflix-eureka-server)</li>
            <li>provides a common place for the other microservices to register</li>
            <li>the Eureka clients register here and then keep sending heartbeats for service health monitoring</li>
        </ul>
    </div>
    <div>
        <ul> 
            <h3>authentication-service</h3>
            <li>a spring boot back-end with a MySQL DB</li>
            <li>on successful authentication hands you a JWT to use for further requests to the other secured microservices</li>
        </ul>
    </div>
    <div>
        <ul> 
            <h3>api-gateway</h3>
            <li>a spring boot application</li>
            <li>routes all requests to the appropriate microservices</li>
            <li>intercepts requests addressed to secured endpoints to validate the JWT</li>
        </ul>
    </div>
    <div>
        <ul> 
            <h3>product-service</h3>
            <li>a spring boot back-end with a MongoDB</li>
            <li>communicates with inventory-service when new products are added</li>
        </ul>
    </div>
    <div>
        <ul> 
            <h3>inventory-service</h3>
            <li>a spring boot back-end with a MySQL DB</li>
            <li>communicates with product-service and order-service</li>
        </ul>
    </div>
    <div>
        <ul> 
            <h3>order-service</h3>
            <li>a spring boot back-end with a MySQL DB</li>
            <li>communicates with inventory-service when new orders are added</li>
        </ul>
    </div>
</body>

</html>