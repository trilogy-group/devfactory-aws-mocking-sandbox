package com.smockin.admin.persistence.dao;

import com.smockin.admin.persistence.entity.RestfulMock;
import com.smockin.admin.persistence.entity.SmockinUser;
import com.smockin.admin.persistence.enums.RecordStatusEnum;
import com.smockin.admin.persistence.enums.RestMethodEnum;
import com.smockin.admin.persistence.enums.RestMockTypeEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.AntPathMatcher;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by mgallina.
 */
@Repository
public class RestfulMockDAOImpl implements RestfulMockDAOCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<RestfulMock> findAllByStatus(final RecordStatusEnum status) {
        return entityManager.createQuery("FROM RestfulMock rm "
                + " WHERE rm.status = :status "
                + " ORDER BY rm.initializationOrder ASC")
                .setParameter("status", status)
                .getResultList();
    }

    @Override
    public List<RestfulMock> findAllByStatusAndUser(final RecordStatusEnum status, final long userId) {
        return entityManager.createQuery("FROM RestfulMock rm "
                + " WHERE rm.createdBy.id = :userId "
                + " AND rm.status = :status "
                + " ORDER BY rm.initializationOrder ASC")
                .setParameter("userId", userId)
                .setParameter("status", status)
                .getResultList();
    }

    @Override
    public List<RestfulMock> findAll() {
        return entityManager.createQuery("FROM RestfulMock rm "
                + " ORDER BY rm.initializationOrder ASC")
                .getResultList();
    }

    @Override
    public List<RestfulMock> findAllByUser(final long userId) {
        return entityManager.createQuery("FROM RestfulMock rm "
                + " WHERE rm.createdBy.id = :userId "
                + " ORDER BY rm.initializationOrder ASC")
                .setParameter("userId", userId)
                .getResultList();
    }

    @Override
    public RestfulMock findByPathAndMethodAndUser(final String path, final RestMethodEnum method, final SmockinUser user) {
        try {
            return entityManager.createQuery("FROM RestfulMock rm "
                    + " WHERE rm.path = :path "
                    + " AND rm.method = :method "
                    + " AND rm.createdBy.id = :userId", RestfulMock.class)
                    .setParameter("path", path)
                    .setParameter("method", method)
                    .setParameter("userId", user.getId())
                    .getSingleResult();
        } catch (Throwable ex) {
            return null;
        }
    }

    @Override
    public RestfulMock findActiveByMethodAndPathPattern(final RestMethodEnum method, final String path) {

        final String part1 = StringUtils.split(path, AntPathMatcher.DEFAULT_PATH_SEPARATOR)[0];

        final List<RestfulMock> mocks = entityManager.createQuery("FROM RestfulMock rm "
                + " WHERE rm.method = :method "
                + " AND (rm.path = :path1 OR rm.path LIKE '/'||:path2||'%')"
                + " AND rm.status = 'ACTIVE'", RestfulMock.class)
                .setParameter("method", method)
                .setParameter("path1", path)
                .setParameter("path2", part1)
                .getResultList();

        return matchPath(mocks, path);
    }

    @Override
    public RestfulMock findActiveByMethodAndPathPatternAndType(final RestMethodEnum method, final String path, final RestMockTypeEnum mockType) {

        final String part1 = StringUtils.split(path, AntPathMatcher.DEFAULT_PATH_SEPARATOR)[0];

        final List<RestfulMock> mocks = entityManager.createQuery("FROM RestfulMock rm "
                + " WHERE rm.method = :method "
                + " AND rm.mockType = :mockType "
                + " AND (rm.path = :path1 OR rm.path LIKE '/'||:path2||'%')"
                + " AND rm.status = 'ACTIVE'", RestfulMock.class)
                .setParameter("method", method)
                .setParameter("mockType", mockType)
                .setParameter("path1", path)
                .setParameter("path2", part1)
                .getResultList();

        return matchPath(mocks, path);
    }

    private RestfulMock matchPath(final List<RestfulMock> mocks, final String path) {

        if (mocks.isEmpty()) {
            return null;
        }

        final AntPathMatcher matcher = new AntPathMatcher(AntPathMatcher.DEFAULT_PATH_SEPARATOR);

        return mocks.stream()
                .filter(m -> matcher.match(m.getPath(), path))
                .findFirst().orElse(null);

    }

}
