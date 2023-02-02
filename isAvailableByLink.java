import sailpoint.object.*;
import sailpoint.api.*;
import sailpoint.util.*;

boolean isAvailableByLink(
      SailPointContext context,
      Identity identity,
      Application application,
      String fieldName,
      String fieldValue) throws GeneralException {
        if (fvLog.isTraceEnabled()) {
          fvLog.trace(
          "Entering isAvailableByLink with identity ["
            + (identity == null ? "" : identity.toString())
            + "] application ["
            + (application == null ? "" : application.toString())
            + "] fieldName ["
            + (fieldName == null ? "" : fieldName)
            + "] fieldValue ["
            + (fieldValue == null ? "" : fieldValue)
            + "]");
        }
        boolean result = false;
        if((context != null) &&
         (identity != null) &&
         (application != null) &&
         (fieldName != null) &&
         (fieldValue != null)) {
          result = true;
          IdentityService identityService = new IdentityService(context);
          List links = identityService.getLinks(identity, application);
          if ((links != null) && !links.isEmpty()) {
            for (Link link : links) {
              if (!result) {
                break;
              }
              Object currentValue = link.getAttribute(fieldName);
              result = !((currentValue instanceof String) && fieldValue.equalsIgnoreCase((String) currentValue));  // if we find a match, the result is false
            }
          }
        }
        if (fvLog.isTraceEnabled()) {
          fvLog.trace("Exiting isAvailableByLink [" + result + "]");
        }
        return result;
      }
