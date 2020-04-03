package com.example.aaddemoapi.controller;

import com.azure.identity.ClientSecretCredential;
import com.azure.identity.ClientSecretCredentialBuilder;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.security.keyvault.secrets.SecretClient;
import com.azure.security.keyvault.secrets.SecretClientBuilder;
import com.azure.security.keyvault.secrets.models.KeyVaultSecret;
import com.example.aaddemoapi.keyvault.ClientSecretKeyVaultCredential;
import com.microsoft.azure.keyvault.KeyVaultClient;
import com.microsoft.azure.keyvault.models.SecretBundle;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/home")
public class UnprotectedController {

    @Value("${keyvault.clientid}")
    private String keyvaultClientId;

    @Value("${keyvault.clientsecret}")
    private String keyvaultClientSecret;

    @Value("${keyvault.tenantid}")
    private String tenantId;

    @Value("${keyvault.keyvaultname}")
    private String keyvaultName;

    public KeyVaultClient keyVaultClient;

    @GetMapping
    public String index() {
        return "{ \"home\": 1}";
    }

    @GetMapping("/keyvaultsp")
    public String getKeyVault () {

        setKeyVaultClientInstance();

        String kvUri = "https://" + keyvaultName + ".vault.azure.net";

        SecretBundle mySecret;

        mySecret = keyVaultClient.getSecret(kvUri, "mySecret");

        if (mySecret == null) {
            keyVaultClient.setSecret(kvUri, "mySecret", "HelloSecret");
        }

        return mySecret.value();
    }


    public KeyVaultClient setKeyVaultClientInstance() {

        if (keyVaultClient==null) {
            keyVaultClient = new KeyVaultClient(new ClientSecretKeyVaultCredential(keyvaultClientId, keyvaultClientSecret));
        }
        return keyVaultClient;
    }

    @GetMapping("/keyvaultsp1")
    public String getKeyVault1 () {

        String kvUri = "https://" + keyvaultName + ".vault.azure.net";

        ClientSecretCredential clientSecretCredential = new ClientSecretCredentialBuilder()
                .clientId(keyvaultClientId)
                .clientSecret(keyvaultClientSecret)
                .tenantId(tenantId)
                .build();

        SecretClient secretClient = new SecretClientBuilder()
                .vaultUrl(kvUri)
                .credential(clientSecretCredential)
                .buildClient();


        secretClient.setSecret(new KeyVaultSecret("mysecret2", "secretvalue"));
        KeyVaultSecret mysecret2 = secretClient.getSecret("mysecret2");

        return mysecret2.getValue();

    }


    @GetMapping("/keyvaultsp2")
    public String getKeyVault2 () {

        String kvUri = "https://" + keyvaultName + ".vault.azure.net";

        SecretClient secretClient = new SecretClientBuilder()
                .vaultUrl(kvUri)
                .credential(new DefaultAzureCredentialBuilder().build())
                .buildClient();

        secretClient.setSecret(new KeyVaultSecret("mysecret2", "secretvalue"));
        KeyVaultSecret mysecret2 = secretClient.getSecret("mysecret2");

        return mysecret2.getValue();

    }


}
