package com.matheusfelixr.sgcc.model.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Embeddable
@Getter
@Setter
public class DataControlImpl implements Serializable {

	private static final long serialVersionUID = -1107231151490523860L;

	@Column(name = "CREATE_DATE", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CREATE_USER", referencedColumnName = "ID", nullable = false)
	private UserAuthentication createUser;

	@Column(name = "UPDATE_DATE", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "UPDATE_USER", referencedColumnName = "ID", nullable = false)
	private UserAuthentication updateUser;

	public void markModified(UserAuthentication updateUser) {
		this.setUpdateDate(new Date());
		this.setUpdateUser(updateUser);
	}

	public void markCreate(UserAuthentication createUser) {
		this.setCreateDate(new Date());
		this.setCreateUser(createUser);
		this.markModified(createUser);
	}
}