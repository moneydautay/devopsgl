package com.greenlucky.backend.service;

import com.greenlucky.backend.persistence.domain.backend.Plan;
import com.greenlucky.backend.persistence.responsitories.PlanRepository;
import com.greenlucky.enums.PlansEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Mr Lam on 8/11/2016.
 */
@Service
@Transactional(readOnly = true)
public class PlanService {

    @Autowired
    private PlanRepository planRepository;

    /**
     * Returns a plan given by plan id or null if was not found
     * @param planId The plan id
     * @return A Plan given by id or null if was not found
     */
    public Plan findByPlanId(int planId){
        return planRepository.findOne(planId);
    }

    /**
     * It creates a Basic or Pro plan
     * @param planId The plan id
     * @return IllegalArgumentException If the plan id is not 1 or 2
     */
    @Transactional
    public Plan createPlan(int planId){

        Plan plan = null;

        if(planId == 1){
            plan = planRepository.save(new Plan(PlansEnum.BASIC));
        }else if(planId == 2){
            plan = planRepository.save(new Plan(PlansEnum.PRO));
        }else
            throw new IllegalArgumentException("Plan id"+ planId + " not recognised");
        return plan;
    }

}
