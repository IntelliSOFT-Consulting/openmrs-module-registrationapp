package org.openmrs.module.registrationapp.fragment.controller.summary;

import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.xdssender.api.domain.Ccd;
import org.openmrs.module.xdssender.api.service.CcdService;
import org.openmrs.ui.framework.annotation.FragmentParam;
import org.openmrs.ui.framework.fragment.FragmentModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ContinuityOfCareFragmentController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContinuityOfCareFragmentController.class);

    public void controller(FragmentModel model, @FragmentParam("patientId") Integer patientId) {

        Patient patient = Context.getPatientService().getPatient(patientId);

        CcdService ccdService = Context.getRegisteredComponent("ccdService", CcdService.class);
        Ccd ccd = ccdService.getLocallyStoredCcd(patient);
        boolean isCCDAvailable = ccd != null;

        model.addAttribute("isCCDAvailable", isCCDAvailable);
        if (isCCDAvailable) {
            model.addAttribute("CCDDate", ccd.getDownloadDate().toString());
        }
    }

    public void viewCCD() {
        LOGGER.info("View CCD");
    }

    public void importCCD(@RequestParam("patientId") Integer patientId, HttpServletResponse response) throws IOException {
        CcdService service = Context.getRegisteredComponent("ccdService", CcdService.class);
        service.downloadCcdAsPDF(response.getOutputStream(), Context.getPatientService().getPatient(patientId));
    }
}
