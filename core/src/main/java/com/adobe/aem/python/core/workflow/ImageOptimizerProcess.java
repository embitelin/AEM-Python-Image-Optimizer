package com.adobe.aem.python.core.workflow;

import com.adobe.aem.python.core.services.ImageOptimizerPythonService;
import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * ImageOptimizerProcess Step which was added to the DAM Update Workflow
 * to send the payloadPath to the ImageOptimzerPythonService
 * @author sruthi.adabettu@embitel.com
 *
 */
@Component(property = {
        Constants.SERVICE_DESCRIPTION + "=Image Optimizer Workflow Process Step",
        Constants.SERVICE_VENDOR + "=Image Optimizer Process",
        "process.label" + "=Image Optimizer Process"
})
public class ImageOptimizerProcess implements WorkflowProcess {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImageOptimizerProcess.class);
    @Reference
    ImageOptimizerPythonService imageOptimizerPythonService;
    @Override
    public void execute(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap metaDataMap) throws WorkflowException {
        LOGGER.info("Inside Custom Process Step");

        String payloadPath = workItem.getWorkflowData().getPayload().toString();
        LOGGER.info("****** Payload Path"+ payloadPath);
        String pythonMessage = null;
        try {
            pythonMessage = imageOptimizerPythonService.getPythonScriptMessage(payloadPath);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        LOGGER.info("******* Workflow Process Step"+ pythonMessage);
    }
}
