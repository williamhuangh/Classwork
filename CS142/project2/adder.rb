# if Adder is missing a method that is called that is in the format of plusnum
# creates the method and prints the initialized value + num

class Adder
  @current
  def initialize(x)
    @current = x
  end

  def current
    @current
  end

  def method_missing(m, *args)
    if (/plus/=~m)==0
      number = m[4, m.length-4]
      if number.to_i.to_s == number
        self.class.class_eval "
          def #{m}()
            num = '#{m}'
            puts self.current+(num[4, num.length-4]).to_i
          end
        "
        eval "#{m}"
      else
        super
      end
    else
      super
    end
  end
end

a = Adder.new(10)
a.plus20
a.plus25
a.minus25
