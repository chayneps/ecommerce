package com.razrmarketinginc.ecommerce.repository;


import com.razrmarketinginc.ecommerce.entity.ENTITY;
import com.razrmarketinginc.ecommerce.util.DateUtil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface AppRepository<E extends ENTITY,I> extends JpaRepository<E,I> {


    default E create(E e){
        return create(e,ENTITY.ACTIVE);
    }

    default E create(E e, String s){
        if(s!=null){
            e.setStatus(s);
        }
        if(e.getCreated()==null)
            e.setCreated(DateUtil.timestampNow());
        e.setLastModified(DateUtil.timestampNow());
        return save(e);
    }

    default E modify(E e){
        e.setLastModified(DateUtil.timestampNow());
        return save(e);
    }

    default E setStatus(I id, String s){
        return this.findById(id).map(e->{
            e.setStatus(s);
            return this.modify(e);
        }).orElse(null);

    }

    default E del(I id){
        return this.findById(id).map(e->{
            e.setStatus(ENTITY.DELETE);
            return this.modify(e);
        }).orElse(null);

    }

}
