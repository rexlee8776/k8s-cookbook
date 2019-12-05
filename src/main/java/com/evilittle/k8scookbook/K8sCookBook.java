package com.evilittle.k8scookbook;

import io.kubernetes.client.ApiClient;
import io.kubernetes.client.ApiException;
import io.kubernetes.client.Configuration;
import io.kubernetes.client.apis.AppsV1Api;
import io.kubernetes.client.apis.CoreV1Api;
import io.kubernetes.client.models.V1Deployment;
import io.kubernetes.client.models.V1Service;
import io.kubernetes.client.models.V1ServiceList;
import io.kubernetes.client.util.Yaml;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class K8sCookBook {
    private static final Logger log = LoggerFactory.getLogger(K8sCookBook.class);
    public static void main(String[] args) {
        log.info("MEP Agent starts.");
        log.debug("Get Kubernetes Client.");
        ApiClient client = null;
        try {
            client = K8sHelper.getK8sClient(Cons.KUBE_CONFIG_PATH);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Configuration.setDefaultApiClient(client);
        // the CoreV1Api loads default api-client from global configuration.
        CoreV1Api api = new CoreV1Api();
        V1ServiceList services = null;
        try {
            services = api.listNamespacedService("mep",null,null,null,null,5,null,60,null);
        } catch (ApiException e) {
            e.printStackTrace();
        }

        log.debug(services.toString());

        Yaml.addModelMap("v1", "Deployment", V1Deployment.class);

        V1Deployment yamlDeploy = null;
        try {
            yamlDeploy = (V1Deployment) Yaml.load(new File("C:/kayak-deploy.yaml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        AppsV1Api appsV1Api = new AppsV1Api();

        try {
            appsV1Api.createNamespacedDeployment("mep",yamlDeploy,null,null,null);
        } catch (ApiException e) {
            e.printStackTrace();
        }

        // Deployment and StatefulSet is defined in apps/v1, so you should use AppsV1Api instead of
        // CoreV1API
//        CoreV1Api api = new CoreV1Api();
        // Example yaml file can be found in $REPO_DIR/test-svc.yaml

        Yaml.addModelMap("v1", "Service", V1Service.class);
        File file = new File("C:/kayak-svc.yaml");
        V1Service yamlSvc = null;
        try {
            yamlSvc = (V1Service) Yaml.load(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        V1Service createResult = null;
        try {
            createResult = api.createNamespacedService("mep", yamlSvc, null, null, null);
        } catch (ApiException e) {
            e.printStackTrace();
        }
        log.debug(createResult.toString());
    }
}
