package com.pulsepoint.hcp365.filter;

import org.springframework.stereotype.Component;

@Component
public class CustomDetailsSecurityFilter{

}
/*public class CustomDetailsSecurityFilter implements Filter {

    @Autowired
    SecurityService securityService;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if(((HttpServletRequest) servletRequest).getRequestURI().indexOf("actuator") == -1) {
            BearerTokenAuthentication authentication = (BearerTokenAuthentication) SecurityContextHolder.getContext().getAuthentication();
            String userName = ((OAuth2IntrospectionAuthenticatedPrincipal) authentication.getPrincipal()).getAttribute("user_name");
            HashMap<String, Object> info = new HashMap<>();
            List<Long> accountIds = securityService.getUserAccounts(userName);
            info.put("accountIds", accountIds);
            boolean isAdmin = securityService.isUserAdmin(userName);
            info.put("isAdmin", isAdmin);
            info.put("userId", securityService.getByUserName(userName).getUserId());
            Long accountId = Long.parseLong(((HttpServletRequest) servletRequest).getHeader("accountId"));
            if (accountId == null) {
                throw new RuntimeException("Invalid request");
            }
            if (isAdmin == true) {
                info.put("accountId", accountId);
            } else {
                if (accountIds.contains(accountId)) {
                    info.put("accountId", accountId);
                } else {
                    throw new RuntimeException("Invalid request");
                }
            }
            authentication.setDetails(info);
        }
        filterChain.doFilter(servletRequest, servletResponse);

    }
}
*/