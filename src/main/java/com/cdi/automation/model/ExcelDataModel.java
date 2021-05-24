package com.cdi.automation.model;

import java.io.Serializable;

public class ExcelDataModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private String subscriptionId;
	private String subscriberOrgId;
	private String subscriberSystemId;
	private String useCaseId;
	private String dataElementId;
	
	private String prosumerOrgId;
	private String prosumerSystemId;
	private String prosumerType;
	
	private String enterpriseSystemId;
	private String enterpriseSystemName;
	private String enterpriseEndPointUrl;
	private String enterpriseS3BucketName;
	private String enterprisePublicKey;
	private String enterpriseOrgId;
	private String enterpriseSystemConfig;
	
	
	private String OrgnizationOrgId;
	private String OrganizationName;
	private String OrganizationIconUrl;
	private String OrganizationType;
	
	public String getenterpriseSystemId() {
		return enterpriseSystemId;
	}
	
	public void setEnterpriseSystemId(String enterpriseSystemId) {
		this.enterpriseSystemId = enterpriseSystemId;
	}
	
	public String getenterpriseSystemname() {
		return enterpriseSystemName;
	}
	
	public void setEnterpriseSystemName(String enterpriseSystemName) {
		this.enterpriseSystemName = enterpriseSystemName;
	}
	
	public String getenterpriseEndPointUrl() {
		return enterpriseEndPointUrl;
	}
	
	public void setEnterpriseEndPointUrl(String enterpriseEndPointURL) {
		this.enterpriseEndPointUrl = enterpriseEndPointUrl;
	}
	
	public String getPublicKey() {
		return enterprisePublicKey;
	}
	
	public void setPublicKey(String enterprisePublicKey) {
		this.enterprisePublicKey = enterprisePublicKey;
	}
	
	public String getenterpriseOrgId() {
		return enterpriseOrgId;
	}
	
	public void setEnterpriseOrgId(String enterpriseOrgId) {
		this.enterpriseOrgId = enterpriseOrgId;
	}
	
	public String getenterpriseS3BucketName() {
		return enterpriseS3BucketName;
	}
	
	public void setEnterpriseS3BucketName(String enterpriseS3BucketName) {
		this.enterpriseS3BucketName = enterpriseS3BucketName;
	}
	
	public String getenterpriseSystemConfig() {
		return enterpriseSystemConfig;
	}
	
	public void setEnterpriseSystemConfig(String enterpriseSystemConfig) {
		this.enterpriseSystemConfig = enterpriseSystemConfig;
	}
	

	public String getSubscriptionId() {
		return subscriptionId;
	}

	public void setSubscriptionId(String subscriptionId) {
		this.subscriptionId = subscriptionId;
	}

	public String getSubscriberOrgId() {
		return subscriberOrgId;
	}

	public void setSubscriberOrgId(String subscriberOrgId) {
		this.subscriberOrgId = subscriberOrgId;
	}


	public String getSubscriberSystemId() {
		return subscriberSystemId;
	}

	public void setSubscriberSystemId(String subscriberSystemId) {
		this.subscriberSystemId = subscriberSystemId;
	}

	public String getUseCaseId() {
		return useCaseId;
	}
	public void setUseCaseId(String useCaseId) {
		this.useCaseId = useCaseId;
	}
	public String getDataElementId() {
		return dataElementId;
	}
	public void setDataElementId(String dataElementId) {
		this.dataElementId = dataElementId;
	}
	public String getProsumerOrgId() {
		return prosumerOrgId;
	}
	public void setProsumerOrgId(String prosumerOrgId) {
		this.prosumerOrgId = prosumerOrgId;
	}

	public String getProsumerType() {
		return prosumerType;
	}

	public void setProsumerType(String prosumerType) {
		this.prosumerType = prosumerType;
	}

	public String getProsumerSystemId() {
		return prosumerSystemId;
	}
	public void setProsumerSystemId(String prosumerSystemId) {
		this.prosumerSystemId = prosumerSystemId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getOrgnizationOrgId() {
		return OrgnizationOrgId;
	}

	public void setOrgnizationOrgId(String orgnizationOrgId) {
		OrgnizationOrgId = orgnizationOrgId;
	}

	public String getOrganizationName() {
		return OrganizationName;
	}

	public void setOrganizationName(String organizationName) {
		OrganizationName = organizationName;
	}

	public String getOrganizationIconUrl() {
		return OrganizationIconUrl;
	}

	public void setOrganizationIconUrl(String organizationIconUrl) {
		OrganizationIconUrl = organizationIconUrl;
	}

	public String getOrganizationType() {
		return OrganizationType;
	}

	public void setOrganizationType(String organizationType) {
		OrganizationType = organizationType;
	}
	
	









}
