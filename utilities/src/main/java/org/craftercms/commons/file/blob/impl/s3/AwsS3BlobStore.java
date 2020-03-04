/*
 * Copyright (C) 2007-2020 Crafter Software Corporation. All Rights Reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3 as published by
 * the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.craftercms.commons.file.blob.impl.s3;

import com.amazonaws.services.s3.AmazonS3;
import org.apache.commons.configuration2.HierarchicalConfiguration;
import org.apache.commons.configuration2.tree.ImmutableNode;
import org.craftercms.commons.aws.S3ClientCachingFactory;
import org.craftercms.commons.config.ConfigurationMapper;
import org.craftercms.commons.config.profiles.aws.S3Profile;
import org.craftercms.commons.file.blob.Blob;
import org.craftercms.commons.file.blob.EnvironmentResolver;
import org.craftercms.commons.file.blob.impl.AbstractBlobStore;
import org.craftercms.commons.spring.resources.S3Resource;
import org.springframework.core.io.Resource;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.prependIfMissing;

/**
 * @author joseross
 */
public class AwsS3BlobStore extends AbstractBlobStore<S3Profile> {

    protected S3ClientCachingFactory clientFactory;

    public void setClientFactory(S3ClientCachingFactory clientFactory) {
        this.clientFactory = clientFactory;
    }

    protected AmazonS3 getClient() {
        return clientFactory.getClient(profile);
    }

    protected String getKey(Mapping mapping, String site, String path) {
        StringBuilder sb = new StringBuilder();
        if (isNotEmpty(mapping.prefix)) {
            sb.append(mapping.prefix).append("/");
        }
        sb.append(site).append(prependIfMissing(path, "/"));
        return  sb.toString();
    }

    @Override
    public void doInit(HierarchicalConfiguration<ImmutableNode> config) {
        // do nothing
    }

    @Override
    protected Resource doGetContent(Mapping mapping, Blob blob) {
        return new S3Resource(clientFactory, profile, mapping.target, blob.getUrl());
    }

}
