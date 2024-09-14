package io.mosip.testrig.dslrig.packetcreator.controller;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.mosip.testrig.dslrig.dataprovider.util.DataProviderConstants;
import io.mosip.testrig.dslrig.packetcreator.dto.PolicyCreateDto;
import io.mosip.testrig.dslrig.packetcreator.service.PolicyManagerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Policy Management Controller", description = "REST Apis for policy management")
public class PolicyManagerController {

	@Value("${mosip.test.persona.configpath}")
	private String policyManagerConfigPath;

	@Autowired
	PolicyManagerService policyManagerService;

	private static final Logger logger = LoggerFactory.getLogger(PolicyManagerController.class);

	@Operation(summary = "Create the new policy group")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Policy group is created successfully") })
	@PostMapping("policies/group/new/{contextKey}")
	public @ResponseBody String createPolicyGroup(@RequestParam(value = "name") String name,
			@RequestParam(value = "desc") String desc, @PathVariable("contextKey") String contextKey) {

		try {
			if (policyManagerConfigPath != null && !policyManagerConfigPath.equals("")) {
				DataProviderConstants.RESOURCE = policyManagerConfigPath;
			}
			return policyManagerService.createPolicyGroup(name, desc, contextKey);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("createPolicyGroup", e);
			return "{\"Failed\"}";
		}

	}

	@Operation(summary = "Retrieve the policies group")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Successfully retrived the policies group") })
	@GetMapping("policies/group/{contextKey}")
	public @ResponseBody String getPolicyGroup(@RequestParam(value = "grpname") String groupname,
			@PathVariable("contextKey") String contextKey) {

		try {
			if (policyManagerConfigPath != null && !policyManagerConfigPath.equals("")) {
				DataProviderConstants.RESOURCE = policyManagerConfigPath;
			}
			return policyManagerService.getPolicyGroupID(groupname, contextKey);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("createPolicyGroup", e);
			return "{\"Failed\"}";
		}

	}

	@Operation(summary = "Create the policy")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successfully created the policy") })
	@PostMapping("policies/{contextKey}")
	public @ResponseBody String createPolicyUnderGroup(@RequestBody PolicyCreateDto policyDetails,
			@PathVariable("contextKey") String contextKey) {

		try {
			if (policyManagerConfigPath != null && !policyManagerConfigPath.equals("")) {
				DataProviderConstants.RESOURCE = policyManagerConfigPath;
			}
			JSONObject policiesJson = new JSONObject();
			policiesJson.put("allowedAuthTypes", policyDetails.getPolicies().getAllowedAuthTypes());
			policiesJson.put("allowedKycAttributes", policyDetails.getPolicies().getAllowedKycAttributes());
			policiesJson.put("authTokenType", policyDetails.getPolicies().getAuthTokenType());
			return policyManagerService.createPolicyUnderGroup(policyDetails.getPolicyGroupName(),
					policyDetails.getName(), policyDetails.getDesc(), policyDetails.getPolicyType(), policiesJson,
					contextKey);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("createPolicyUnderGroup", e);
			return "{\"Failed\"}";
		}

	}

	@Operation(summary = "Publish the policy group")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Policy group is published successfully") })
	@PostMapping("/policies/{policyId}/group/{policygroupId}/publish/{contextKey}")
	public @ResponseBody String publishPolicy(@PathVariable("policyId") String policyId,
			@PathVariable("policygroupId") String policygroupId, @PathVariable("contextKey") String contextKey) {

		try {
			if (policyManagerConfigPath != null && !policyManagerConfigPath.equals("")) {
				DataProviderConstants.RESOURCE = policyManagerConfigPath;
			}
			return policyManagerService.publishPolicy(policyId, policygroupId, contextKey);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("createPolicyUnderGroup", e);
			return "{\"Failed\"}";
		}

	}

}
