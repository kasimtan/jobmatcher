package com.jobmatcher.domain;

import com.jobmatcher.reference.ExperienceLevel;
import com.jobmatcher.reference.Industry;
import com.jobmatcher.reference.JobType;
import com.jobmatcher.service.JobService;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.roo.addon.dod.RooDataOnDemand;
import org.springframework.stereotype.Component;

@Component
@Configurable
@RooDataOnDemand(entity = Job.class)
public class JobDataOnDemand {

	private Random rnd = new SecureRandom();

	private List<Job> data;

	@Autowired
    HiringManagerDataOnDemand hiringManagerDataOnDemand;

	@Autowired
    JobService jobService;

	public Job getNewTransientJob(int index) {
        Job obj = new Job();
        setCompanyDescription(obj, index);
        setDesiredSkills(obj, index);
        setExperienceLevel(obj, index);
        setHiringManager(obj, index);
        setIndustry(obj, index);
        setJobDescription(obj, index);
        setJobExpirationDate(obj, index);
        setJobPostedDate(obj, index);
        setJobTitle(obj, index);
        setJobType(obj, index);
        return obj;
    }

	public void setCompanyDescription(Job obj, int index) {
        String companyDescription = "companyDescription_" + index;
        if (companyDescription.length() > 255) {
            companyDescription = companyDescription.substring(0, 255);
        }
        obj.setCompanyDescription(companyDescription);
    }

	public void setDesiredSkills(Job obj, int index) {
        String desiredSkills = "desiredSkills_" + index;
        if (desiredSkills.length() > 255) {
            desiredSkills = desiredSkills.substring(0, 255);
        }
        obj.setDesiredSkills(desiredSkills);
    }

	public void setExperienceLevel(Job obj, int index) {
        ExperienceLevel experienceLevel = ExperienceLevel.class.getEnumConstants()[0];
        obj.setExperienceLevel(experienceLevel);
    }

	public void setHiringManager(Job obj, int index) {
        HiringManager hiringManager = hiringManagerDataOnDemand.getRandomHiringManager();
        obj.setHiringManager(hiringManager);
    }

	public void setIndustry(Job obj, int index) {
        Industry industry = Industry.class.getEnumConstants()[0];
        obj.setIndustry(industry);
    }

	public void setJobDescription(Job obj, int index) {
        String jobDescription = "jobDescription_" + index;
        if (jobDescription.length() > 255) {
            jobDescription = jobDescription.substring(0, 255);
        }
        obj.setJobDescription(jobDescription);
    }

	public void setJobExpirationDate(Job obj, int index) {
        Date jobExpirationDate = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setJobExpirationDate(jobExpirationDate);
    }

	public void setJobPostedDate(Job obj, int index) {
        Date jobPostedDate = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setJobPostedDate(jobPostedDate);
    }

	public void setJobTitle(Job obj, int index) {
        String jobTitle = "jobTitle_" + index;
        if (jobTitle.length() > 255) {
            jobTitle = jobTitle.substring(0, 255);
        }
        obj.setJobTitle(jobTitle);
    }

	public void setJobType(Job obj, int index) {
        JobType jobType = JobType.class.getEnumConstants()[0];
        obj.setJobType(jobType);
    }

	public Job getSpecificJob(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        Job obj = data.get(index);
        BigInteger id = obj.getId();
        return jobService.findJob(id);
    }

	public Job getRandomJob() {
        init();
        Job obj = data.get(rnd.nextInt(data.size()));
        BigInteger id = obj.getId();
        return jobService.findJob(id);
    }

	public boolean modifyJob(Job obj) {
        return false;
    }

	public void init() {
        int from = 0;
        int to = 10;
        data = jobService.findJobEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'Job' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<Job>();
        for (int i = 0; i < 10; i++) {
            Job obj = getNewTransientJob(i);
            try {
                jobService.saveJob(obj);
            } catch (ConstraintViolationException e) {
                StringBuilder msg = new StringBuilder();
                for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                    ConstraintViolation<?> cv = iter.next();
                    msg.append("[").append(cv.getConstraintDescriptor()).append(":").append(cv.getMessage()).append("=").append(cv.getInvalidValue()).append("]");
                }
                throw new RuntimeException(msg.toString(), e);
            }
            data.add(obj);
        }
    }
}
