package com.evilittle.k8scookbook;

import io.kubernetes.client.ApiClient;
import io.kubernetes.client.util.ClientBuilder;
import io.kubernetes.client.util.KubeConfig;

import java.io.FileReader;
import java.io.IOException;

public class K8sHelper {

    public static ApiClient getK8sClient(String configPath) throws IOException {

        // loading the out-of-cluster config, a kubeconfig from file-system
        ApiClient client =
                ClientBuilder.kubeconfig(KubeConfig.loadKubeConfig(new FileReader(configPath))).build();
        return client;
    }
}
