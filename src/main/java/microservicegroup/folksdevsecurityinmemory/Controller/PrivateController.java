package microservicegroup.folksdevsecurityinmemory.Controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/Private")
public class PrivateController
{
    @GetMapping
    public String helloWorld()
    {
        return "Hello World from Private Endpoint";
    }
    @PreAuthorize("hasAuthority('User')")
    @GetMapping("/user")
    public String helloWorldwithUser()
    {
        return "Hello World from Private User Auth  Endpoint";
    }
    @PreAuthorize("hasAuthority('Admin')")
    @GetMapping("/admin")
    public String helloWorldwithAdmin()
    {
        return "Hello World from Private AdminUser Auth  Endpoint";
    }
}
