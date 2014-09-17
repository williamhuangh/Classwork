class UsersController < ApplicationController
  def index()
    @users = User.find(:all)
  end
  def show()
    @user = User.find(params[:id])
    @photos = @user.photos
  end
  def login
    if !session[:log].nil?
      redirect_to users_path
    else
      @user = User.find(:all)
      @error = params[:invalid]
      @password = params[:password]
    end
  end
  def post_login
    @username = User.find_by_login(params[:login_val])
    if @username != nil
      if @username.password_valid?(params[:passwordAttempt])
        session[:log] = @username
        redirect_to users_path(@username)
      else
        redirect_to "/users/login?password=false"
      end
    else
      redirect_to "/users/login?invalid=true"
    end
  end
  def logout
    reset_session
    flash.notice = "You have logged out."
    redirect_to user_path(:login)
  end
  def new
    if @user.nil?
      @user = User.new
    end
  end
  def create
    @user = User.new(user_params(params[:user]))
    if @user.valid?
      @user.save()
      flash.notice = "New user created."
      redirect_to user_path(:login)
    else
      render :new
    end
  end
  private
    def user_params(params)
      return params.permit(:first_name, :last_name, :login, :password, :password_confirmation)
    end
end
