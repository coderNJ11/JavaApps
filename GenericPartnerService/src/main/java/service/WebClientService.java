package service;

import lombok.extern.slf4j.Slf4j;
import model.APIDetails;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.support.GenericApplicationContext;
import utils.GenericConstants;

import javax.inject.Inject;
import javax.inject.Named;

@Named
@Slf4j
public class WebClientService {

    private final GenericApplicationContext applicationContext;

    @Inject
    public WebClientService(GenericApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
    public ServiceCaller getServiceCaller(APIDetails apiDetails) {
        try{
            return applicationContext.getBean(apiDetails.getApiName(), ServiceCaller.class);
        }
        catch(NoSuchBeanDefinitionException e) {
            applicationContext.registerBean(apiDetails.getApiName(), ServiceCaller.class,
                    ()-> createServiceCaller(apiDetails), (bd) -> bd.setLazyInit(true));
            return applicationContext.getBean(apiDetails.getApiName(), ServiceCaller.class);
        }
    }

    private ServiceCaller  createServiceCaller(APIDetails apiDetails) {
        return null;
    }
}
