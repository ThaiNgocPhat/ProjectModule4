package ra.md4.service.order;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ra.md4.dao.order.IOrderDao;
import ra.md4.models.Order;
import ra.md4.models.User;

import java.util.List;

@Service
public class OrderServiceImpl implements IOrderService {
    @Autowired
    private IOrderDao iOrderDao;
    @Override
    public void save(Order order) {
        iOrderDao.save(order);
    }

    @Override
    public List<Order> findByUser(User user) {
        return iOrderDao.findByUser(user);
    }
}
