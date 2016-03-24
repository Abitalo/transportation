package dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public abstract class BaseDao <T>{
	private SessionFactory sessionFactory;
	private Configuration cfg;
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	//��û��ʹ��Spring֮ǰ����Ҫ�Լ�����һ��SessionFactory
	public BaseDao() {
		super();
		cfg = new Configuration().configure();
        StandardServiceRegistry build = new StandardServiceRegistryBuilder().applySettings(cfg.getProperties()).build();
        sessionFactory = cfg.buildSessionFactory(build);
	}
	
	@SuppressWarnings("unchecked")
	public T get(Class<T> entityClass,Serializable id){
		return (T) sessionFactory.openSession().get(entityClass, id);
	}
	
	public Serializable save(T entity){
		return sessionFactory.openSession().save(entity);
	}
	
	public void update(T entity){
		getSessionFactory().openSession().saveOrUpdate(entity);
	}
	
	public void delete(T entity){
		 getSessionFactory().openSession().delete(entity);
	}
	
	@SuppressWarnings("unchecked")
	public boolean delete(Class<T> entityClass,Serializable id){
		T entity = (T) getSessionFactory().openSession().get(entityClass, id);
		if(entity != null){
			getSessionFactory().openSession().delete(entity);
			return true;
		}
		else return false;
	}
	
	// ��ȡ����ʵ��
	public List<T> findAll(Class<T> entityClazz)
	{
		return find("select en from "
			+ entityClazz.getSimpleName() + " en");
	}
	
	// ��ȡʵ������
	public long findCount(Class<T> entityClazz)
	{
		List<?> list = find("select count(*) from "
			+ entityClazz.getSimpleName());
		// ���ز�ѯ�õ���ʵ������
		if (list != null && list.size() == 1 )
		{
			return (Long)list.get(0);
		}
		return 0;
	}

	// ����HQL����ѯʵ��
	@SuppressWarnings("unchecked")
	protected List<T> find(String hql)
	{
		return (List<T>)getSessionFactory().openSession()
			.createQuery(hql)
			.list();
	}
	// ���ݴ�ռλ������HQL����ѯʵ��
	@SuppressWarnings("unchecked")
	protected List<T> find(String hql , Object... params)
	{
		// ������ѯ
		Query query = getSessionFactory().openSession()
			.createQuery(hql);
		// Ϊ����ռλ����HQL������ò���
		for(int i = 0 , len = params.length ; i < len ; i++)
		{
			query.setParameter(i + "" , params[i]);
		}
		return (List<T>)query.list();
	}
	/**
	 * ʹ��hql �����з�ҳ��ѯ����
	 * @param hql ��Ҫ��ѯ��hql���
	 * @param pageNo ��ѯ��pageNoҳ�ļ�¼
	 * @param pageSize ÿҳ��Ҫ��ʾ�ļ�¼��
	 * @return ��ǰҳ�����м�¼
	 */
	@SuppressWarnings("unchecked")
	protected List<T> findByPage(String hql,
		 int pageNo, int pageSize)
	{
		// ������ѯ
		return getSessionFactory().openSession()
			.createQuery(hql)
			// ִ�з�ҳ
			.setFirstResult((pageNo - 1) * pageSize)
			.setMaxResults(pageSize)
			.list();
	}
	/**
	 * ʹ��hql �����з�ҳ��ѯ����
	 * @param hql ��Ҫ��ѯ��hql���
	 * @param params ���hql��ռλ��������params���ڴ���ռλ������
	 * @param pageNo ��ѯ��pageNoҳ�ļ�¼
	 * @param pageSize ÿҳ��Ҫ��ʾ�ļ�¼��
	 * @return ��ǰҳ�����м�¼
	 */
	@SuppressWarnings("unchecked")
	protected List<T> findByPage(String hql , int pageNo, int pageSize
		, Object... params)
	{
		// ������ѯ
		Query query = getSessionFactory().openSession()
			.createQuery(hql);
		// Ϊ����ռλ����HQL������ò���
		for(int i = 0 , len = params.length ; i < len ; i++)
		{
			query.setParameter(i + "" , params[i]);
		}
		// ִ�з�ҳ�������ز�ѯ���
		return query.setFirstResult((pageNo - 1) * pageSize)
			.setMaxResults(pageSize)
			.list();
	}
}
