package cz.komix.qsystem.backend.service;

import cz.komix.qsystem.backend.exception.ISAAAInterfaceNotFound;
import cz.komix.qsystem.backend.exception.ISAAARequestFailed;
import cz.komix.qsystem.backend.logic.usecases.IQSystemUseCases;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * List of methods that communicate with IS AAAA.
 *
 * @author Jan Lejnar
 */
@RestController
@Named
public class MessagesDispatcherService {

    private IQSystemUseCases useCases;

    @Inject
    public MessagesDispatcherService(IQSystemUseCases useCases) {
        this.useCases = useCases;
    }

    public StandardQueryResponseData dispatchStandardQuery(String reason, StandardQueryType searchCriteria) throws ISAAARequestFailed, ISAAAInterfaceNotFound {
        return useCases.dispatchStandardQuery(reason, searchCriteria);
    }

    public StandardQueryResponseData dispatchComplementQuery(String reason, AAAIDType AAAID) throws ISAAARequestFailed, ISAAAInterfaceNotFound {
        return useCases.dispatchComplementQuery(reason, AAAID);
    }

    public String dispatchLoadAttachments(String reason, AAAIDType AAAID, long dataId) throws ISAAARequestFailed, ISAAAInterfaceNotFound {
        return useCases.dispatchLoadAttachments(reason, AAAID, dataId);
    }

    public String dispatchLoadLinks(String reason, AAAIDType AAAID, long dataId) throws ISAAARequestFailed, ISAAAInterfaceNotFound {
        return useCases.dispatchLoadLinks(reason, AAAID, dataId);
    }
}
