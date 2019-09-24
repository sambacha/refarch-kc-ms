# Voyage Management Service

!!! Abstract
        This service keeps track of each scheduled, current or completed voyage of container carreer vessels, being loaded with containers at a source port, sailing to a destination port and having onboard containers unloaded there.


## Build

The script `.script/buildDocker.sh` build the docker image for this service.

## Run locally as Node.js application

This is more for development purpose in your sandbox.

```bash
npm install
npm test
npm start
```

## Deployment prerequisites

Regardless of specific deployment targets (OCP, IKS, k8s), the following prerequisite Kubernetes artifacts need to be created to support the deployments of application components.  These artifacts need to be created once per unique deployment of the entire application and can be shared between application components in the same overall application deployment.

1. Create `kafka-brokers` ConfigMap
  - Command: `kubectl create configmap kafka-brokers --from-literal=brokers='<replace with comma-separated list of brokers>' -n <namespace>`
  - Example: `kubectl create configmap kafka-brokers --from-literal=brokers='broker-3-j7fxtxtp5fs84205.kafka.svc01.us-south.eventstreams.cloud.ibm.com:9093,broker-2-j7fxtxtp5fs84205.kafka.svc01.us-south.eventstreams.cloud.ibm.com:9093,broker-1-j7fxtxtp5fs84205.kafka.svc01.us-south.eventstreams.cloud.ibm.com:9093,broker-5-j7fxtxtp5fs84205.kafka.svc01.us-south.eventstreams.cloud.ibm.com:9093,broker-0-j7fxtxtp5fs84205.kafka.svc01.us-south.eventstreams.cloud.ibm.com:9093,broker-4-j7fxtxtp5fs84205.kafka.svc01.us-south.eventstreams.cloud.ibm.com:9093' -n eda-refarch`
2. Create optional `eventstreams-apikey` Secret, if you are using Event Streams as your Kafka broker provider
  - Command: `kubectl create secret generic eventstreams-apikey --from-literal=binding='<replace with api key>' -n <namespace>`
  - Example: `kubectl create secret generic eventstreams-apikey --from-literal=binding='z...12345...notanactualkey...67890...a' -n eda-refarch`

## Deploy on IBM Kubernetes Service

```sh
# to install the helm release under browncompute namespace
$ helm install voyagesms/ --name kc-voyagesms --namespace browncompute
# To see the pod deployment
$ kubectl describe pod voyagesms  --namespace browncompute
# To get the exposed port
$ kubectl get service kc-voyagesms
$ kubectl get service voyagesms-application-service -n browncompute
# Status and traced of the pods:
$ kubectl logs fleetms-deployment-58b7d58fb8-qcqz7  -n browncompute
# then point your URL to the ipaddress and port number:
http://
```

## Deploy to OpenShift Container Platform (OCP)

### Deploy to OCP 3.11

**Cross-component deployment prerequisites:** _(needs to be done once per unique deployment of the entire application)_
1. If desired, create a non-default Service Account for usage of deploying and running the K Container reference implementation.  This will become more important in future iterations, so it's best to start small:
  - Command: `oc create serviceaccount -n <target-namespace> kcontainer-runtime`
  - Example: `oc create serviceaccount -n eda-refarch kcontainer-runtime`
2. The target Service Account needs to be allowed to run containers as `anyuid` for the time being:
  - Command: `oc adm policy add-scc-to-user anyuid -z <service-account-name> -n <target-namespace>`
  - Example: `oc adm policy add-scc-to-user anyuid -z kcontainer-runtime -n eda-refarch`
  - NOTE: This requires `cluster-admin` level privileges.

**Perform the following for the `SpringContainerMS` microservice:**
1. Build and push Docker image
  1. Create a Jenkins project, pointing to the remote GitHub repository for the Order Microservices, creating the necessary parameters.  Refer to the individual microservice's `Jenkinsfile.NoKubernetesPlugin` for appropriate parameter values.
    - Create a String parameter named `REGISTRY` to determine a remote registry that is accessible from your cluster.
    - Create a String parameter named `REGISTRY_NAMESPACE` to describe the registry namespace to push image into.
    - Create a String parameter named `IMAGE_NAME` which should be self-expalantory.
    - Create a String parameter named `CONTEXT_DIR` to determine the correct working directory to work from inside the source code, with respect to the root of the repository.
    - Create a String parameter named `DOCKERFILE` to determine the desired Dockerfile to use to build the Docker image.  This is determined with respect to the `CONTEXT_DIR` parameter.
    - Create a Credentials parameter named `REGISTRY_CREDENTIALS` and assign the necessary credentials to allow Jenkins to push the image to the remote repository.
  2. Manually build the Docker image and push it to a registry that is accessible from your cluster (Docker Hub, IBM Cloud Container Registry, manually deployed Quay instance):
    - `docker build -t <private-registry>/<image-namespace>/kc-voyages-ms:latest order-command-ms/`
    - `docker login <private-registry>`
    - `docker push <private-registry>/<image-namespace>/kc-voyages-ms:latest`
3. Generate application YAMLs via `helm template`:
  - Parameters:
    - `--set image.repository=<private-registry>/<image-namespace>/<image-repository>`
    - `--set image.tag=latest`
    - `--set image.pullSecret=<private-registry-pullsecret>` (optional or set to blank)
    - `--set image.pullPolicy=Always`
    - `--set eventstreams.env=ICP`
    - `--set eventstreams.brokersConfigMap=<kafka brokers ConfigMap name>`
    - `--set eventstreams.apikeyConfigMap=<kafka api key Secret name>`
    - `--set serviceAccountName=<service-account-name>`
    - `--namespace <target-namespace>`
    - `--output-dir <local-template-directory>`
  - Example: `helm template --set image.repository=rhos-quay.internal-network.local/browncompute/kc-voyages-ms --set image.tag=latest --set image.pullSecret= --set image.pullPolicy=Always --set eventstreams.env=ICP --set eventstreams.brokersConfigMap=kafka-brokers --set eventstreams.apikeyConfigMap=kafka-apikey --set serviceAccountName=kcontainer-runtime --output-dir templ --namespace eda-refarch chart/voyagesms/`
4. Deploy application using `oc apply`:
  - `oc apply -f templates/voyagesms/templates`
