package cz.komix.qsystem.frontend.controller.callService;

import cz.komix.qsystem.backend.exception.ISAAAInterfaceNotFound;
import cz.komix.qsystem.backend.exception.ISAAARequestFailed;
import cz.komix.qsystem.backend.service.MessagesDispatcherService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author Jan Lejnar
 */
@Named
@ApplicationScoped
public class MessagesDispatcherController {

    private MessagesDispatcherService messagesDispatcherService;

    @Inject
    public MessagesDispatcherController(MessagesDispatcherService messagesDispatcherService) {
        this.messagesDispatcherService = messagesDispatcherService;
    }

    public StandardQueryResponseData dispatchStandardQuery(String reason, StandardQueryType searchCriteria) throws ISAAARequestFailed, ISAAAInterfaceNotFound {
        return messagesDispatcherService.dispatchStandardQuery(reason, searchCriteria);
    }

    public StandardQueryResponseData dispatchComplementQuery(String reason, AAAIDType AAAID) throws ISAAARequestFailed, ISAAAInterfaceNotFound {
        return messagesDispatcherService.dispatchComplementQuery(reason, AAAID);
    }

    public String dispatchLoadAttachments(String reason, AAAIDType AAAID, long dataId) throws ISAAARequestFailed, ISAAAInterfaceNotFound {
        return messagesDispatcherService.dispatchLoadAttachments(reason, AAAID, dataId);
    }

    public String dispatchLoadLinks(String reason, AAAIDType AAAID, long dataId) throws ISAAARequestFailed, ISAAAInterfaceNotFound {
        return messagesDispatcherService.dispatchLoadLinks(reason, AAAID, dataId);
    }
}
