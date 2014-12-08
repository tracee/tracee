package io.tracee.spring;

import org.springframework.stereotype.Service;

/**
 * @author <a href="carl.duevel@holisticon.de">Carl Anders Düvel</a>
 */
@Service
public class MySpringServiceBean implements MySpringService {
    @Override
    @LogContextOnException
    public void somethingBadHappens() {
        throw new RuntimeException("plonk!");
    }

    @Override
    @LogContextOnException
    public void everythingIsAlright() {

    }
}
